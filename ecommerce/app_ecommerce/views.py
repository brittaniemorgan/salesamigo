from flask import current_app as app, jsonify, render_template, url_for, flash, redirect, request, abort
from app_ecommerce import get_db_connection
from flask_login import login_user, logout_user, current_user, login_required
from app_ecommerce.forms import LoginForm, SignUpForm, LoyaltyProgramsForm
from app_ecommerce.models import db, Customers, Product, Category, Brand, ProductVariant, Size, LoyaltyPrograms, ProductInventory, SalesTransactions, TransactionItems
from datetime import datetime, timezone, timedelta
from flask_mail import Message
from flask_mail import Mail
gmt_offset = -5
gmt_offset_td = timedelta(hours=gmt_offset)

@app.route('/')
def home():
    return render_template('home.html')
    
@app.route('/login', methods=['GET', 'POST'])
def login():
    form = LoginForm()
    if form.validate_on_submit():
        email = form.email.data
        password = form.password.data
        user = Customers.query.filter_by(email=email).first()
        if user is not None:
            login_user(user)
            if user.password == password:
                flash('You have been logged in successfully!', 'success')
                return redirect(url_for('shop'))
            else:
                flash('Incorrect Password', 'danger')
        else:
            flash('Incorrect Email', 'danger')
    return render_template('login.html', form=form)

@app.route('/signup', methods=['POST', 'GET'])
def signup():
    form = SignUpForm()
    if form.validate_on_submit():
        firstname = form.firstname.data
        lastname = form.lastname.data
        email = form.email.data
        contact_number = form.contact_number.data
        password = form.password.data
        address = form.address.data
        points_balance = 0

        customer = Customers(firstname, lastname, email, contact_number,
                            password, address, points_balance, 
                            date_joined=datetime.now(timezone(timedelta(hours=gmt_offset))))
        try:
            db.session.add(customer)
            db.session.commit()
            flash('You have signed up successfully!', 'success')
            return redirect(url_for('login'))
        except Exception as e:
            db.session.rollback()
            flash('You could not sign up.', 'danger')
    return render_template('signup.html', form=form)

@app.route('/shop', methods=['POST', 'GET'])
@login_required
def shop():
    query = request.args.get('query')
    products = Product.query.all()
    if query:
        product = Product.query.join(ProductVariant).join(Size).filter(Product.product_id == product_id).first()
        if not products:
            flash("No products found.", 'danger')
    return render_template('shop.html', products=products)

@app.route('/product/<int:product_id>')
@login_required
def product(product_id):
    product = Product.query.get_or_404(product_id)
    size = request.args.get('size')
    color = request.args.get('color')
    min_quantity = request.args.get('min_quantity')
    return render_template('product.html', product=product, size=size, color=color, min_quantity=min_quantity)

@app.route('/shop/cart')
@login_required
def cart():
    return render_template('cart.html')

@app.route('/shop/cart/checkout', methods=['POST', 'GET'])
@login_required
def checkout():
    return render_template('checkout.html', customer=current_user)

@app.route('/checkout/ok', methods=['POST'])
def place_order():
    data = request.get_json()

    if not data or 'cart' not in data or 'totalItems' not in data or 'finalPrice' not in data:
            return jsonify({"error": "Invalid data format"}), 400

    else:
        cart = data['cart']
        total_items = data['totalItems']
        final_price = data['finalPrice']

        order = SalesTransactions(current_user.customer_id, final_price, 'Credit Card', 550, datetime.now(timezone(timedelta(hours=gmt_offset))))

        db.session.add(order)
        db.session.commit()

        for item in cart:
            transaction_item = TransactionItems(
                transaction_id=order.transaction_id, product_id=int(item['id']),
                quantity=float(total_items), price=float(final_price)
            )
            db.session.add(transaction_item)
        db.session.commit()

        return jsonify({"message": "Order has been placed successfully!", "redirect_url": url_for('orders')}), 200
            

@app.route('/orders', methods=['GET'])
@login_required
def orders():
    orders = SalesTransactions.query.filter_by(customer_id=current_user.customer_id).all()
    return render_template('orders.html', orders=orders)

@app.route('/rewards', methods=['POST', 'GET'])
@login_required
def rewards():
    form = LoyaltyProgramsForm()
    mail = Mail()
    if form.validate_on_submit():
        firstname = form.firstname.data
        lastname = form.lastname.data
        email = form.email.data
        contact_number = form.contact_number.data
        email = form.email.data
        points = 10
        customer_id = current_user.customer_id

        rewards = LoyaltyPrograms("Medium", points, "Valued Customer", customer_id)
        
        db.session.add(rewards)
        db.session.commit()
        flash('You have successfully signed up for Rewards!', 'success')

        customer = Customers.query.get(customer_id)
        customer.points_balance += points
        db.session.commit()
            
        subject = "Welcome to Sales Amigo Rewards Program!"
        message = f"Dear {firstname},\n\nYou have successfully signed up for Rewards at Sales Amigo. You have received 10 points.\n\nAfter purchasing from Sales Amigo 10 times, you will receive additional rewards points you can use.\n\nThank you!\n\nSales Amigo Team"
        msg = Message(subject=subject, sender='rewards@salesamigo.com', recipients=[email])
        msg.body = message
        mail.send(msg)
        return redirect(url_for('shop'))
    return render_template('rewards.html', form=form)

@app.route('/logout')
@login_required
def logout():
    logout_user()
    flash('You have been successfully logged out!', 'success')
    return redirect(url_for('login'))