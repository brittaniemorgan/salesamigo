from flask import Flask, render_template, request, redirect, url_for, jsonify, send_from_directory, abort
from flask_wtf.csrf import CSRFProtect
from flask_wtf.csrf import generate_csrf
import os
import mysql.connector
from datetime import datetime

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your_secret_key_here'  # Set your secret key here
app.config['UPLOAD_FOLDER'] = 'images'  # Set the path to your uploaded images directory
csrf = CSRFProtect(app)

app.config['WTF_CSRF_ENABLED'] = False

def get_db_connection():
    connection = mysql.connector.connect(
    host = 'localhost',
    user= 'swen3920',
    password = 'password123',
    database = 'sales_amigo')
    return connection


@app.route('/')
def hello():
    data = {'message': 'Hello from API'}
    return jsonify(data)

@app.route('/register_user', methods=['POST'])
def register():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()
        content = request.json
        username = content['username']
        password = content['password']
        role = content['role']
        query = "INSERT INTO User (username, password, role) VALUES (%s, %s, %s)"
        cursor.execute(query, (username, password, role))
        connection.commit()

        return jsonify({"success" : "User added"})

    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/login', methods=['POST'])
def login():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        content = request.json
        employee_id = content['employee_id']
        password = content['password']
        cursor.execute('SELECT * FROM employees WHERE employee_id = %s', (employee_id,))
        user = cursor.fetchone()
        if user is None:
            return jsonify({"error": "Invalid Employee ID"}), 400
        
        cursor.execute('SELECT * FROM employees WHERE employee_id = %s AND password = %s', (employee_id, password))
        user = cursor.fetchone()

        if user is not None:
           # user.pop('password', None)
            return jsonify({"message": "User logged in successfully",
                            "user":user}), 200
        else:
            return jsonify({"error": "Invalid password"}), 400  

    except Exception as e:
        return jsonify({"error": str(e)})
    

@app.route('/employees', methods=['GET'])
def get_users():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        query = f"SELECT * FROM employees"
        cursor.execute(query)
        result = cursor.fetchall()

        users = []
        for user in result:
            users.append(user)
        cursor.close()
        connection.close()
        return jsonify(users=users) 

    except Exception as e:
        return jsonify({"error": str(e)})

@app.route('/customers', methods=['GET'])
def get_customers():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        query = "SELECT * FROM customers"
        cursor.execute(query)
        customers = cursor.fetchall()
        cursor.close()
        connection.close()
        return jsonify(customers=customers), 200

    except Exception as e:
        return jsonify({"error": str(e)})


@app.route('/customers/<int:id>', methods=['GET'])
def get_customer(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        query = "SELECT * FROM customers WHERE customer_id = %s"
        cursor.execute(query, (id,))
        customers = cursor.fetchall()
        cursor.close()
        connection.close()
        return jsonify(customers=customers), 200

    except Exception as e:
        return jsonify({"error": str(e)})

@app.route('/csrf-token', methods=['GET']) 
def get_csrf(): 
    return jsonify({'csrf_token': generate_csrf()}) 

@app.route('/products', methods=['POST'])
def add_item():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        category = content['category']
        price = content['price']
        description = content['description']
        brand = content['brand']
        gender = content['gender']

        query = """INSERT INTO product (name, category_id, price, description, brand_id, gender) 
                VALUES (%s, %s, %s, %s, %s, %s)"""
        cursor.execute(query, (name, category, price, description, brand, gender))
        connection.commit()
        id = cursor.lastrowid
        cursor.close()
        connection.close()
        return jsonify({"success": "Product added", "id": id})
    
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/products', methods=['PUT'])
def update_product():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        product_id = content['product_id']
        name = content['name']
        category = content['category_id']
        price = content['price']
        description = content['description']
        brand = content['brand_id']
        gender = content['gender']
        query = "SELECT * FROM product WHERE product_id = %s"
        cursor.execute(query, (product_id,))
        product = cursor.fetchone()

        if not product:
            return jsonify({"error": f"Product with ID {product_id} not found"}), 404

        query = """UPDATE product SET 
                          name = %s,
                          category_id = %s,
                          price = %s,
                          description = %s,
                          brand_id = %s,
                          gender = %s
                          WHERE product_id = %s"""

        cursor.execute(query, (name, category, price, description, brand, gender, product_id))
        connection.commit()

        return jsonify({"success": "Product updated successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/products', methods=['DELETE'])
def delete_product():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        product_id = content['product_id']
        query = "SELECT * FROM product WHERE product_id = %s"
        cursor.execute(query, (product_id,))
        product = cursor.fetchone()

        if not product:
            return jsonify({"error": f"Product with ID {product_id} not found"}), 404

        query = """DELETE FROM product WHERE product_id = %s"""

        cursor.execute(query, (product_id,))
        connection.commit()

        return jsonify({"success": "Product deleted successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/products', methods=['GET'])
def view_products():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = """SELECT * FROM product p
                    JOIN brand b ON p.brand_id = b.brand_id
                    JOIN category c ON c.category_id = p.category_id"""
        cursor.execute(query)
        products = cursor.fetchall()

        return jsonify({"products": products}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/categories', methods=['POST'])
def add_category():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM category WHERE category = %s"
        cursor.execute(query, (name,))
        category = cursor.fetchone()

        if category:
            return jsonify({"error": f"Size with Category {name} already exists"}), 404

        query = "INSERT INTO category (category) VALUES (%s)"
        cursor.execute(query, (name,))
        connection.commit()
        id = cursor.lastrowid
        cursor.close()
        connection.close()
        return jsonify({"success": "Category added", "id":id})
    
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/categories/<int:id>', methods=['PUT'])
def update_category(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM category WHERE category_id = %s"
        cursor.execute(query, (id,))
        category = cursor.fetchone()

        if not category:
            return jsonify({"error": f"Category with ID {id} not found"}), 404

        query = """UPDATE category SET 
                          category = %s
                          WHERE category_id = %s"""

        cursor.execute(query, (name, id))
        connection.commit()

        return jsonify({"success": "Category updated successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/categories/<int:id>', methods=['DELETE'])
def delete_category(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        query = "SELECT * FROM category WHERE category_id = %s"
        cursor.execute(query, (id,))
        category = cursor.fetchone()

        if not category:
            return jsonify({"error": f"Category with ID {id} not found"}), 404

        query = "DELETE FROM category WHERE category_id = %s"

        cursor.execute(query, (id, ))
        connection.commit()

        return jsonify({"success": "Category deleted successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/categories', methods=['GET'])
def view_categories():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM category"
        cursor.execute(query)
        categories = cursor.fetchall()

        return jsonify({"categories": categories}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/brands', methods=['POST'])
def add_brand():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM brand WHERE brand = %s"
        cursor.execute(query, (name,))
        brand = cursor.fetchone()

        if brand:
            return jsonify({"error": f"Brand with name {name} already exists"}), 404


        query = "INSERT INTO brand (brand) VALUES (%s)"
        cursor.execute(query, (name,))
        connection.commit()
        id = cursor.lastrowid
        cursor.close()
        connection.close()
        return jsonify({"success": "Brand added", "id":id})
    
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/brands/<int:id>', methods=['PUT'])
def update_brand(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM brand WHERE brand_id = %s"
        cursor.execute(query, (id,))
        brand = cursor.fetchone()

        if not brand:
            return jsonify({"error": f"Brand with ID {id} not found"}), 404

        query = """UPDATE brand SET 
                          brand = %s
                          WHERE brand_id = %s"""

        cursor.execute(query, (name, id))
        connection.commit()

        return jsonify({"success": "Brand updated successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/brands/<int:id>', methods=['DELETE'])
def delete_brand(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        query = "SELECT * FROM brand WHERE brand_id = %s"
        cursor.execute(query, (id,))
        brand = cursor.fetchone()

        if not brand:
            return jsonify({"error": f"Brand with ID {id} not found"}), 404

        query = "DELETE FROM brand WHERE brand_id = %s"

        cursor.execute(query, (id, ))
        connection.commit()

        return jsonify({"success": "Brand deleted successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/brands', methods=['GET'])
def view_brands():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM brand"
        cursor.execute(query)
        brands = cursor.fetchall()

        return jsonify({"brands": brands}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/sizes', methods=['POST'])
def add_size():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM size WHERE size = %s"
        cursor.execute(query, (name,))
        size = cursor.fetchone()

        if size:
            return jsonify({"error": f"Size with name {name} already exists"}), 404

        query = "INSERT INTO size (size) VALUES (%s)"
        cursor.execute(query, (name,))
        connection.commit()
        id = cursor.lastrowid
        cursor.close()
        connection.close()
        return jsonify({"success": "Size added", "id":id})
    
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/sizes/<int:id>', methods=['PUT'])
def update_size(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM size WHERE size_id = %s"
        cursor.execute(query, (id,))
        size = cursor.fetchone()

        if not size:
            return jsonify({"error": f"Size with ID {id} not found"}), 404

        query = """UPDATE size SET 
                          size = %s
                          WHERE size_id = %s"""

        cursor.execute(query, (name, id))
        connection.commit()

        return jsonify({"success": "size updated successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/sizes/<int:id>', methods=['DELETE'])
def delete_size(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        query = "SELECT * FROM size WHERE size_id = %s"
        cursor.execute(query, (id,))
        size = cursor.fetchone()

        if not size:
            return jsonify({"error": f"Size with ID {id} not found"}), 404

        query = "DELETE FROM size WHERE size_id = %s"

        cursor.execute(query, (id, ))
        connection.commit()

        return jsonify({"success": "Size deleted successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/sizes', methods=['GET'])
def view_sizes():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM size"
        cursor.execute(query)
        sizes = cursor.fetchall()

        return jsonify({"sizes": sizes}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/product_variants', methods=['POST'])
def add_product_variants():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        content = request.json
        product_id = content['product_id']
        size = content['size']
        color = content['color']
        quantity = content['quantity']
        price = content['price']
        min_quantity = content['min_quantity']

        insert_query = """
            INSERT INTO ProductVariant (product_id, size_id, color, quantity, price, min_quantity)
            VALUES (%s, %s, %s, %s, %s, %s)
        """
        cursor.execute(insert_query, (product_id, size, color, quantity, price, min_quantity))
        connection.commit()
        id = cursor.lastrowid
        cursor.close()

        return jsonify({"success": "Product variation added successfully", "id" : id}), 200
    
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/product_variants/<int:variant_id>', methods=['PUT'])
def update_product_variants(variant_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        content = request.json
        size = content['size']
        color = content['color']
        quantity = content['quantity']
        price = content['price']
        min_quantity = content['min_quantity']

        query = "SELECT * FROM ProductVariant WHERE variant_id = %s"
        cursor.execute(query, (variant_id,))
        variant = cursor.fetchone()

        if not variant:
            return jsonify({"error": f"Variant with ID {variant_id} not found"}), 404
        
        update_query = """
            UPDATE ProductVariant
            SET size_id = %s, color = %s, quantity = %s, price = %s, min_quantity = %s
            WHERE variant_id = %s
        """
        cursor.execute(update_query, (size, color, quantity, price, min_quantity, variant_id))
        connection.commit()
        cursor.close()

        return jsonify({"success": "Product variation updated successfully"}), 200
    
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/product_variants/<int:variant_id>', methods=['DELETE'])
def delete_product_variants(variant_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM ProductVariant WHERE variant_id = %s"
        cursor.execute(query, (variant_id,))
        variant = cursor.fetchone()

        if not variant:
            return jsonify({"error": f"Variant with ID {variant_id} not found"}), 404

        delete_query = "DELETE FROM ProductVariant WHERE variant_id = %s"
        cursor.execute(delete_query, (variant_id,))
        connection.commit()
        cursor.close()

        return jsonify({"success": "Product variation deleted successfully"}), 200
    
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/product_variants', methods=['GET'])
def get_product_variants():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM ProductVariant pv JOIN Size s ON s.size_id = pv.size_id"
        cursor.execute(query)
        product_variations = cursor.fetchall()

        return jsonify({"product_variations": product_variations}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/payment_types', methods=['POST'])
def add_payment_type():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM payment_type WHERE payment = %s"
        cursor.execute(query, (name,))
        payment_type = cursor.fetchone()

        if payment_type:
            return jsonify({"error": f"Payment type {name} already exists"}), 404

        query = "INSERT INTO payment_type (payment) VALUES (%s)"
        cursor.execute(query, (name,))
        connection.commit()
        id = cursor.lastrowid
        cursor.close()
        connection.close()
        return jsonify({"success": "Payment type added", "id": id})

    except Exception as e:
        return jsonify({'error': str(e)})


@app.route('/payment_types/<int:id>', methods=['PUT'])
def update_payment_type(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        name = content['name']
        query = "SELECT * FROM payment_type WHERE payment_id = %s"
        cursor.execute(query, (id,))
        payment_type = cursor.fetchone()

        if not payment_type:
            return jsonify({"error": f"Payment type with ID {id} not found"}), 404

        query = """UPDATE payment_type SET 
                          payment = %s
                          WHERE payment_id = %s"""

        cursor.execute(query, (name, id))
        connection.commit()

        return jsonify({"success": "Payment type updated successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/payment_types/<int:id>', methods=['DELETE'])
def delete_payment_type(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        query = "SELECT * FROM payment_type WHERE payment_id = %s"
        cursor.execute(query, (id,))
        payment_type = cursor.fetchone()

        if not payment_type:
            return jsonify({"error": f"Payment type with ID {id} not found"}), 404

        query = "DELETE FROM payment_type WHERE payment_id = %s"

        cursor.execute(query, (id,))
        connection.commit()

        return jsonify({"success": "Payment type deleted successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/payment_types', methods=['GET'])
def view_payment_types():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = "SELECT * FROM payment_type"
        cursor.execute(query)
        payment_types = cursor.fetchall()

        return jsonify({"payment_types": payment_types}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/discounts', methods=['GET'])
def get_discounts():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        
        cursor.execute("""
            SELECT *
            FROM discounts WHERE discount_type = "GENERAL" """)
        general_discounts = cursor.fetchall()

        cursor.execute("""SELECT d.*, product_id
            FROM discounts d
            RIGHT JOIN product_discounts pd ON d.discount_id = pd.discount_id
            WHERE (d.end_date >= NOW())""")
        product_discounts = cursor.fetchall()

        cursor.execute("""SELECT d.*, category_id
            FROM discounts d
            RIGHT JOIN category_discounts cd ON d.discount_id = cd.discount_id
            WHERE (d.end_date >= NOW())""")
        category_discounts = cursor.fetchall()

        cursor.execute("""SELECT d.*, brand_id
            FROM discounts d
            RIGHT JOIN brand_discounts bd ON d.discount_id = bd.discount_id
            WHERE (d.end_date >= NOW())""")
        brand_discounts = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify({"general discounts": general_discounts,
                        "product discounts" : product_discounts,
                        "category discounts": category_discounts,
                        "brand discounts" : brand_discounts
                        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400



#@app.route('/discounts/<int:product_id>', methods=['GET'])
def get_item_discounts(product_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Fetch category and brand of the product
        cursor.execute("""
            SELECT category_id, brand_id
            FROM product p
            INNER JOIN productvariant pv ON pv.product_id = p.product_id
            WHERE pv.variant_id = %s
        """, (product_id,))
        product_info = cursor.fetchone()

        if not product_info:
            return jsonify({"error": "Product not found"}), 404

        #product_id = product_info['product_id']
        category_id = product_info['category_id']
        brand_id = product_info['brand_id']

        # Fetch discounts applicable to the product based on category, brand, and general store discounts
        cursor.execute("""
            SELECT *
            FROM discounts WHERE discount_type = "GENERAL"

              UNION 

            SELECT d.*
            FROM discounts d
            LEFT JOIN product_discounts pd ON d.discount_id = pd.discount_id
            WHERE ((pd.product_id = %s)
              AND d.end_date >= NOW())

              UNION 

            SELECT d.*
            FROM discounts d
            LEFT JOIN category_discounts cd ON d.discount_id = cd.discount_id
            WHERE ((cd.category_id = %s)
              AND d.end_date >= NOW())

                UNION 

            SELECT d.*
            FROM discounts d
            LEFT JOIN brand_discounts bd ON d.discount_id = bd.discount_id
            WHERE ((bd.brand_id = %s)
              AND d.end_date >= NOW())
        """, (product_id, category_id, brand_id))

        discounts = cursor.fetchall()

        cursor.close()
        connection.close()

        return {"discounts": discounts}
    except Exception as e:
        return {"error": str(e)}

def get_highest_discount_for_item(product_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        current_date = datetime.now().date()

        # Fetch all applicable discounts for the product
        query = """
            SELECT discount_id, discount_amount 
            FROM discounts d
            JOIN product_discounts pd ON d.discount_id = pd.discount_id
            WHERE pd.product_id = %s 
              AND d.start_date <= %s 
              AND d.end_date >= %s
            ORDER BY discount_amount DESC
        """
        cursor.execute(query, (product_id, current_date, current_date))
        discounts = cursor.fetchall()

        cursor.close()
        connection.close()

        if discounts:
            max_discount = max(discounts, key=lambda x: x['discount_amount'])
            return jsonify(discounts=discounts)
        
        return 0, None

    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/sales_transactions', methods=['POST'])
def create_sales_transaction():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        
        connection.start_transaction()  # Start a new transaction

        content = request.json
        transaction_date = datetime.now()
        employee_id = content['employee_id']
        customer_id = content.get('customer_id', 1)
        total = content['total']
        payment_method = content['payment_method']
        items = content['items']
        pointsApplied = content.get('pointsApplied', 0)

        # Check inventory before proceeding
        for item in items:
            product_id = item['product_id']
            quantity = item['quantity']
            
            # Fetch current quantity from inventory
            fetch_inventory_query = "SELECT quantity FROM productvariant WHERE variant_id = %s"
            cursor.execute(fetch_inventory_query, (product_id,))
            current_quantity = cursor.fetchone()

            if not current_quantity or current_quantity['quantity'] < quantity:
                return jsonify({"error": f"Insufficient inventory for product ID {product_id}"}), 400

            # Check if the discount applies to this product
            discount_id = item.get('discount_id', 0)
            if discount_id == 0:
                continue
            discounts = get_item_discounts(product_id)
            if "error" in discounts:
                return jsonify({"error": discounts['error']}), 400

            applicable_discount = next((discount for discount in discounts['discounts'] if discount['discount_id'] == discount_id), None)
    
            if not applicable_discount:
                return jsonify({"error": f"Discount ID {discount_id} does not apply to product ID {product_id}"}), 400

       # Check if customer exists and has enough points
        if customer_id > 1:
            fetch_customer_query = """
                SELECT points_balance FROM customers WHERE customer_id = %s
            """
            cursor.execute(fetch_customer_query, (customer_id,))
            customer = cursor.fetchone()

            if not customer:
                return jsonify({"error": "Customer does not exist"}), 400

            if customer['points_balance'] < pointsApplied:
                return jsonify({"error": "Insufficient points"}), 400

            # Update customer points balance
            update_customers_query = """
                UPDATE customers
                SET points_balance = points_balance - %s + %s
                WHERE customer_id = %s
            """
            cursor.execute(update_customers_query, (pointsApplied, total / 10, customer_id))


        # Insert into sales_transactions table
        insert_transaction_query = """
            INSERT INTO sales_transactions (transaction_date, employee_id, customer_id, total, payment_method)
            VALUES (%s, %s, %s, %s, %s)
        """
        cursor.execute(insert_transaction_query, (transaction_date, employee_id, customer_id, total, payment_method))
        transaction_id = cursor.lastrowid
        
        # Insert into transaction_items table
        for item in items:
            product_id = item['product_id']
            quantity = item['quantity']
            price = item['price']
            discount_id = item.get('discount_id')

            insert_item_query = """
                INSERT INTO transaction_items (transaction_id, product_id, quantity, price, discount_id)
                VALUES (%s, %s, %s, %s, %s)
            """
            cursor.execute(insert_item_query, (transaction_id, product_id, quantity, price, discount_id))

            # Reduce inventory quantity
            update_inventory_query = """
                UPDATE productvariant
                SET quantity = quantity - %s
                WHERE variant_id = %s
            """
            cursor.execute(update_inventory_query, (quantity, product_id))
        
        connection.commit()  # Commit the transaction if all operations succeed
        cursor.close()
        connection.close()

        return jsonify({"success": "Transaction created successfully", "transaction_id": transaction_id}), 200

    except Exception as e:
        connection.rollback()  # Roll back the transaction if any error occurs
        cursor.close()
        connection.close()
        return jsonify({"error": str(e)}), 400




@app.route('/sales_transactions', methods=['GET'])
def get_sales_transactions():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        cursor.execute("SELECT * FROM sales_transactions")
        transactions = cursor.fetchall()

        for transaction in transactions:
            transaction_id = transaction['transaction_id']
            cursor.execute("SELECT * FROM transaction_items WHERE transaction_id = %s", (transaction_id,))
            items = cursor.fetchall()
            transaction['items'] = items

        cursor.close()
        connection.close()

        return jsonify({"sales_transactions": transactions}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/sales_transactions/<int:transaction_id>', methods=['GET'])
def get_sales_transaction(transaction_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        cursor.execute("SELECT * FROM sales_transactions WHERE transaction_id = %s", (transaction_id,))
        transaction = cursor.fetchone()

        if not transaction:
            return jsonify({"error": "Transaction not found"}), 404

        cursor.execute("SELECT * FROM transaction_items WHERE transaction_id = %s", (transaction_id,))
        items = cursor.fetchall()
        transaction['items'] = items

        cursor.close()
        connection.close()

        return jsonify(transaction), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400

################################## MAY NOT NEED #########################################
@app.route('/sales_transactions/<int:transaction_id>/refund', methods=['POST'])
def process_refund(transaction_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        # Fetch original transaction details
        fetch_transaction_query = "SELECT * FROM sales_transactions WHERE transaction_id = %s"
        cursor.execute(fetch_transaction_query, (transaction_id,))
        transaction = cursor.fetchone()

        if not transaction:
            return jsonify({"error": f"Transaction with ID {transaction_id} not found"}), 404

        # Check if the transaction is eligible for refund (add your own conditions here)
        # For example, you might check if it's within a certain timeframe, etc.
        if not transaction['is_refundable']:
            return jsonify({"error": "This transaction is not eligible for a refund"}), 400

        # Calculate refund amount based on items in the transaction
        fetch_items_query = "SELECT * FROM transaction_items WHERE transaction_id = %s"
        cursor.execute(fetch_items_query, (transaction_id,))
        items = cursor.fetchall()

        total_refund = 0.0
        for item in items:
            # Adjust inventory if returning items to stock
            # Update your inventory records accordingly
            # Assuming your item table has price and quantity
            total_refund += item['price'] * item['quantity']

        # Update sales_transactions to mark as refunded
        update_transaction_query = """
            UPDATE sales_transactions 
            SET is_refunded = 1
            WHERE transaction_id = %s
        """
        cursor.execute(update_transaction_query, (transaction_id,))

        # Insert a new transaction for the refund
        insert_refund_query = """
            INSERT INTO sales_transactions (transaction_date, employee_id, customer_id, total, payment_method, is_refund, original_transaction_id)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        """
        cursor.execute(insert_refund_query, (datetime.now(), transaction['employee_id'], transaction['customer_id'], -total_refund, 'Refund', 1, transaction_id))

        connection.commit()
        cursor.close()
        connection.close()

        return jsonify({"success": "Refund processed successfully", "amount_refunded": total_refund}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400

##########################MAY NOT BE NECESSARY######################################
@app.route('/sales_transactions/<int:transaction_id>', methods=['PUT'])
def update_sales_transaction(transaction_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        total = content['total']
        payment_method = content['payment_method']
        
        # Update the transaction details
        update_transaction_query = """
            UPDATE sales_transactions
            SET total = %s, payment_method = %s
            WHERE transaction_id = %s
        """
        cursor.execute(update_transaction_query, (total, payment_method, transaction_id))

        # Delete all existing items for the transaction
        delete_items_query = "DELETE FROM transaction_items WHERE transaction_id = %s"
        cursor.execute(delete_items_query, (transaction_id,))

        # Insert new items for the transaction
        items = content['items']
        for item in items:
            product_id = item['product_id']
            quantity = item['quantity']
            price = item['price']
            
            insert_item_query = """
                INSERT INTO transaction_items (transaction_id, product_id, quantity, price)
                VALUES (%s, %s, %s, %s)
            """
            cursor.execute(insert_item_query, (transaction_id, product_id, quantity, price))
        
        connection.commit()
        cursor.close()
        connection.close()

        return jsonify({"success": "Transaction updated successfully"}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/sales_transactions/<int:transaction_id>', methods=['DELETE'])
def delete_sales_transaction(transaction_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        # Delete items associated with the transaction
        delete_items_query = "DELETE FROM transaction_items WHERE transaction_id = %s"
        cursor.execute(delete_items_query, (transaction_id,))

        # Delete the transaction itself
        delete_transaction_query = "DELETE FROM sales_transactions WHERE transaction_id = %s"
        cursor.execute(delete_transaction_query, (transaction_id,))

        connection.commit()
        cursor.close()
        connection.close()

        return jsonify({"success": "Transaction deleted successfully"}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400
#############################################################################

@app.route('/sales_transactions/refunds', methods=['GET'])
def get_refunds():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        query = """
            SELECT * FROM sales_transactions
            WHERE is_refund = 1
        """
        cursor.execute(query)
        refunds = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify({"refunds": refunds}), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 400
############################################################################################################

if __name__ == '__main__':
    app.run(debug=True)
