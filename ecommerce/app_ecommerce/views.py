from flask import current_app as app, jsonify, render_template, url_for, flash, redirect, request, abort
from app_ecommerce import get_db_connection
from flask_login import login_user, logout_user, current_user, login_required
from app_ecommerce.forms import LoginForm, SignUpForm, RewardsForm
from app_ecommerce.models import db, Customers, Product, Category, Brand, ProductVariant, Size, Rewards, ProductInventory, SalesTransaction, TransactionItems
from datetime import datetime
from flask_mail import Message
from flask_mail import Mail

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
                            date_joined=datetime.utcnow())
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
# @login_required
def shop():
    query = request.args.get('query')
    products = Product.query.all()
    if query:
        product = Product.query.join(ProductVariant).join(Size).filter(Product.product_id == product_id).first()
        # products = Product.query.filter(Product.name.ilike(f'%{query}%')).all()
        if not products:
            flash("No products found.", 'danger')
    return render_template('shop.html', products=products)

@app.route('/product/<int:product_id>')
# @login_required
def product(product_id):
    product = Product.query.get_or_404(product_id)
    size = request.args.get('size')
    color = request.args.get('color')
    min_quantity = request.args.get('min_quantity')
    return render_template('product.html', product=product, size=size, color=color, min_quantity=min_quantity)

@app.route('/shop/cart')
# @login_required
def cart():
    return render_template('cart.html')

@app.route('/shop/cart/checkout', methods=['POST', 'GET'])
# @login_required
def checkout():
    if request.method == 'POST':
        cart = session.get('cart', [])
        total_items = session.get('total_items', 0)
        final_price = session.get('final_price', 0.0)

        customer_id = current_customer_id  # Replace with your actual customer ID logic

        # Create a new sales transaction
        sales_transaction = SalesTransaction(customer_id=customer_id, total=final_price,
                            payment_method='Credit Card', transaction_date=datetime.utcnow())
        db.session.add(sales_transaction)
        db.session.commit()

        # Add transaction items to the database
        for item in cart:
            transaction_item = TransactionItem(transaction_id=sales_transaction.transaction_id,
                            product_id=item['product_id'], quantity=item['quantity'],
                            price=item['price'])
            db.session.add(transaction_item)

        db.session.commit()

        # Clear session variables
        session.pop('cart', None)
        session.pop('total_items', None)
        session.pop('final_price', None)

        return jsonify({'success': True, 'message': 'Order placed successfully!'})  # Return JSON response

    # Handle GET requests or other cases (optional)
    return render_template('checkout.html', customer=current_user)

@app.route('/orders', methods=['GET'])
# @login_required
def orders():
    if current_user.is_authenticated:
        transaction = SalesTransaction.query.filter_by(employee_id=550, customer_id=current_user.customer_id).all()
        return render_template('orders.html', transaction=transaction)
        if not current_user.is_authenticated:
            flash('You need to be logged in to view orders.', 'danger')
    return redirect(url_for('login')) 


@app.route('/rewards', methods=['POST', 'GET'])
# @login_required
def rewards():
    form = RewardsForm()
    mail = Mail()
    if form.validate_on_submit():
        firstname = form.firstname.data
        lastname = form.lastname.data
        email = form.email.data
        contact_number = form.contact_number.data
        rewards_balance = 10
        customer_id = current_user.customer_id

        rewards = Rewards(customer_id, firstname, lastname, email, contact_number, rewards_balance)
        try:
            db.session.add(rewards)
            db.session.commit()
            flash('You have successfully signed up for Rewards!', 'success')

            customer = Customers.query.get(customer_id)
            customer.points_balance += rewards_balance
            db.session.commit()
            
            subject = "Welcome to Sales Amigo Rewards Program!"
            message = f"Dear {firstname},\n\nYou have successfully signed up for Rewards at Sales Amigo. You have received 10 points.\n\nAfter purchasing from Sales Amigo 10 times, you will receive additional rewards points you can use.\n\nThank you!\n\nSales Amigo Team"
            msg = Message(subject=subject, sender='rewards@salesamigo.com', recipients=[email])
            msg.body = message
            mail.send(msg)
            return redirect(url_for('shop'))
        except Exception as e:
            db.session.rollback()
            flash('You could not sign up for Rewards.', 'danger')
    return render_template('rewards.html', form=form)

@app.route('/logout')
@login_required
def logout():
    logout_user()
    flash('You have been successfully logged out!', 'success')
    return redirect(url_for('login'))