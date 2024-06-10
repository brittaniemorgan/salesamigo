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


# Define a dictionary to map discount types to their corresponding table names
DISCOUNT_TABLES = {
    'GENERAL': 'discounts',
    'BRAND': 'brand_discounts',
    'CATEGORY': 'category_discounts',
    'PRODUCT': 'product_discounts'
}

@app.route('/discounts', methods=['POST'])
def add_discount():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        discount_type = content['discount_type']
        discount_code = content['discount_code']
        discount_name = content['discount_name']
        discount_percent = content['discount_percent']
        start_date = content['start_date']
        end_date = content['end_date']
        
        # Validate discount type
        if discount_type not in DISCOUNT_TABLES:
            return jsonify({"error": f"Invalid discount type: {discount_type}"}), 400

        table_name = DISCOUNT_TABLES[discount_type]

        # Insert into discounts table
        query = f"""
            INSERT INTO discounts (discount_code, discount_name, discount_percent, start_date, end_date, discount_type) 
            VALUES (%s, %s, %s, %s, %s, %s)
        """
        cursor.execute(query, (discount_code, discount_name, discount_percent, start_date, end_date, discount_type))
        discount_id = cursor.lastrowid

        # Insert into corresponding discount type table
        if discount_type == 'GENERAL':
            pass  # No additional tables for general discounts
        
        else:
            # Extract the IDs from the request content
            ids = content['applicable_ids']
            for id_ in ids:
                query = f"INSERT INTO {table_name} ({discount_type.lower()}_id, discount_id) VALUES (%s, %s)"
                cursor.execute(query, (id_, discount_id))

        connection.commit()

        cursor.close()
        connection.close()
        return jsonify({"success": f"{discount_type.capitalize()} discount added", "id": discount_id}), 200
    
    except Exception as e:
        return jsonify({'error': str(e)}), 400

@app.route('/discounts/<int:id>', methods=['PUT'])
def update_discount(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        content = request.json
        discount_type = content['discount_type']
        discount_code = content['discount_code']
        discount_name = content['discount_name']
        discount_percent = content['discount_percent']
        start_date = content['start_date']
        end_date = content['end_date']
        
        # Validate discount type
        if discount_type not in DISCOUNT_TABLES:
            return jsonify({"error": f"Invalid discount type: {discount_type}"}), 400

        table_name = DISCOUNT_TABLES[discount_type]

        query = "SELECT discount_id FROM discounts WHERE discount_id = %s"
        cursor.execute(query, (id,))
        result = cursor.fetchone()
        if not result:
            return jsonify({"error": f"Discount with ID {id} not found"}), 
            
        # Update discounts table
        query = """
            UPDATE discounts SET 
                discount_code = %s, 
                discount_name = %s, 
                discount_percent = %s, 
                start_date = %s, 
                end_date = %s, 
                discount_type = %s 
            WHERE discount_id = %s
        """
        cursor.execute(query, (discount_code, discount_name, discount_percent, start_date, end_date, discount_type, id))

        # Insert into corresponding discount type table
        if discount_type == 'GENERAL':
            pass  # No additional tables for general discounts
        
        else:
            # Extract the IDs from the request content            
            #ids = content[f'{discount_type.lower()}_ids']
            ids = content['applicable_ids']
            
            # Delete existing brand_discounts for this discount_id
            query = f"DELETE FROM {table_name} WHERE discount_id = %s"
            cursor.execute(query, (id,))

            for id_ in ids:
                query = f"INSERT INTO {table_name} ({discount_type.lower()}_id, discount_id) VALUES (%s, %s)"
                cursor.execute(query, (id_, id))

        connection.commit()

        cursor.close()
        connection.close()
        return jsonify({"success": f"{discount_type.capitalize()} discount updated successfully"}), 200
    
    except Exception as e:
        return jsonify({'error': str(e)}), 400


@app.route('/discounts/<int:id>', methods=['DELETE'])
def delete_discount(id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor()

        # Get discount_id from brand_discounts table
        query = "SELECT discount_id, discount_type FROM discounts WHERE discount_id = %s"
        cursor.execute(query, (id,))
        result = cursor.fetchone()
        if not result:
            return jsonify({"error": f"{discount_type.capitalize()} discount with ID {id} not found"}), 404
        
        discount_type = result[1]

         # Validate discount type
        if discount_type not in DISCOUNT_TABLES:
            return jsonify({"error": f"Invalid discount type: {discount_type}"}), 400

        table_name = DISCOUNT_TABLES[discount_type]

        # Delete from relevant table
        query_delete = f"DELETE FROM {table_name} WHERE discount_id = %s"
        cursor.execute(query_delete, (id,))

        # Delete from discounts table
        query_delete_discount = "DELETE FROM discounts WHERE discount_id = %s"
        cursor.execute(query_delete_discount, (id,))
        connection.commit()

        cursor.close()
        connection.close()
        return jsonify({"success": f"{discount_type.capitalize()} discount deleted successfully"}), 200
    
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

        # Fetch all sales transactions
        cursor.execute("SELECT * FROM sales_transactions")
        transactions = cursor.fetchall()

        for transaction in transactions:
            transaction_id = transaction['transaction_id']

            # Fetch transaction items
            cursor.execute("SELECT * FROM transaction_items WHERE transaction_id = %s", (transaction_id,))
            items = cursor.fetchall()
            transaction['items'] = items

            # Fetch refund items associated with the transaction
            cursor.execute("SELECT * FROM refunds WHERE transaction_id = %s", (transaction_id,))
            refund_items = cursor.fetchall()
            transaction['refund_items'] = refund_items

        cursor.close()
        connection.close()

        return jsonify({"transactions": transactions}), 200

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

@app.route('/refunds/<int:transaction_id>', methods=['POST'])
def process_refund(transaction_id):
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)
        
        connection.start_transaction()  # Start a new transaction

        content = request.json
        items_to_refund = content.get('items', [])
        employee_id = content.get('employee_id')
        amount = content.get('amount')

        # Fetch the transaction details
        fetch_transaction_query = """
            SELECT * FROM sales_transactions
            WHERE transaction_id = %s
        """
        cursor.execute(fetch_transaction_query, (transaction_id,))
        transaction = cursor.fetchone()

        if not transaction:
            return jsonify({"error": "Transaction not found"}), 404

        # Fetch items from the original transaction
        fetch_items_query = """
            SELECT ti.*, COALESCE(SUM(r.quantity), 0) as refunded_quantity
            FROM transaction_items ti
            LEFT JOIN refunds r ON ti.transaction_item_id = r.transaction_item_id
            WHERE ti.transaction_id = %s
            GROUP BY ti.transaction_item_id
        """
        cursor.execute(fetch_items_query, (transaction_id,))
        original_items = cursor.fetchall()

        # Create a dictionary of original items for easy lookup
        original_items_dict = {item['transaction_item_id']: item for item in original_items}

        total_refunded_amount = 0

        # Validate and process the items to be refunded
        for item in items_to_refund:
            transaction_item_id = item['transaction_item_id']
            quantity_to_refund = item['quantity']
            restock = item.get('restock', False)
            amount = item['quantity'] * item['price']

            if transaction_item_id not in original_items_dict:
                return jsonify({"error": f"Transaction Item ID {transaction_item_id} not found in original transaction {transaction_id}"}), 400

            original_item = original_items_dict[transaction_item_id]
            available_quantity = original_item['quantity'] - original_item['refunded_quantity']

            if quantity_to_refund > available_quantity:
                return jsonify({"error": f"Cannot refund more than purchased quantity for Transaction Item ID {transaction_item_id}"}), 400

            price = original_item['price']

            total_refunded_amount += price * quantity_to_refund

            # Restore inventory quantity if restock is True
            if restock:
                update_inventory_query = """
                    UPDATE productvariant
                    SET quantity = quantity + %s
                    WHERE variant_id = %s
                """
                cursor.execute(update_inventory_query, (quantity_to_refund, original_item['product_id']))

            # Record the refunded items
            refund_item_query = """
                INSERT INTO refunds (transaction_id, transaction_item_id, employee_id, quantity, restock, amount)
                VALUES (%s, %s, %s, %s, %s, %s)
            """
            cursor.execute(refund_item_query, (transaction_id, transaction_item_id, employee_id, quantity_to_refund, restock, amount))

    
        # Update customer points balance if applicable
        cursor.execute('SELECT customer_id from sales_transactions WHERE transaction_id = %s', (transaction_id,))
        customer_id = cursor.fetchone()
        if transaction['customer_id'] > 1:
            points_to_restore = total_refunded_amount / 10
            update_customer_query = """
                UPDATE customers
                SET points_balance = points_balance - %s
                WHERE customer_id =  %s)
            """
            cursor.execute(update_customer_query, (points_to_restore, customer_id))

        connection.commit()  # Commit the transaction if all operations succeed
        cursor.close()
        connection.close()

        return jsonify({"success": "Items refunded successfully"}), 200

    except Exception as e:
        connection.rollback()  # Roll back the transaction if any error occurs
        cursor.close()
        connection.close()
        return jsonify({"error": str(e)}), 400


@app.route('/sales_report/basic', methods=['GET'])
def sales_report_basic():
    try:
        # Get the start_date and end_date from query parameters
        start_date = request.args.get('start_date')
        end_date = request.args.get('end_date')

        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date_obj = datetime.strptime(start_date, date_format)
            end_date_obj = datetime.strptime(end_date, date_format)
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Query to fetch sales data within the given date range
        query = """
            SELECT 
                st.transaction_id,
                st.transaction_date,
                st.employee_id,
                st.customer_id,
                st.total AS transaction_total,
                st.payment_method,
                st.discount_id AS transaction_discount_id,
                ti.transaction_item_id,
                ti.product_id,
                ti.quantity,
                ti.price AS item_price,
                ti.discount_id AS item_discount_id,
                p.name AS product_name,
                p.category_id,
                p.brand_id,
                pv.variant_id,
                pv.size_id,
                pv.color
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
            ORDER BY st.transaction_date, st.transaction_id, ti.transaction_item_id
        """
        cursor.execute(query, (start_date, end_date))
        sales_data = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify(sales_data=sales_data), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/sales_report', methods=['GET'])
def sales_report():
    try:
        # Get the filter parameters from query parameters
        start_date = request.args.get('start_date')
        end_date = request.args.get('end_date')
        employee_id = request.args.get('employee_id')
        customer_id = request.args.get('customer_id')
        payment_method = request.args.get('payment_method')
        product_id = request.args.get('product_id')
        category_id = request.args.get('category_id')
        brand_id = request.args.get('brand_id')
        discount_code = request.args.get('discount_code')
        min_amount = request.args.get('min_amount')
        max_amount = request.args.get('max_amount')

        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date_obj = datetime.strptime(start_date, date_format)
            end_date_obj = datetime.strptime(end_date, date_format)
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Base query with dynamic filters
        query = """
            SELECT 
                st.transaction_id,
                st.transaction_date,
                st.employee_id,
                st.customer_id,
                st.total AS transaction_total,
                st.payment_method,
                st.discount_id AS transaction_discount_id,
                ti.transaction_item_id,
                ti.product_id,
                ti.quantity,
                ti.price AS item_price,
                ti.discount_id AS item_discount_id,
                p.name AS product_name,
                p.category_id,
                p.brand_id,
                pv.variant_id,
                pv.size_id,
                pv.color
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
        """
        params = [start_date, end_date]

        # Add filters dynamically
        if employee_id:
            query += " AND st.employee_id = %s"
            params.append(employee_id)
        if customer_id:
            query += " AND st.customer_id = %s"
            params.append(customer_id)
        if payment_method:
            query += " AND st.payment_method = %s"
            params.append(payment_method)
        if product_id:
            query += " AND ti.product_id = %s"
            params.append(product_id)
        if category_id:
            query += " AND p.category_id = %s"
            params.append(category_id)
        if brand_id:
            query += " AND p.brand_id = %s"
            params.append(brand_id)
        if discount_code:
            query += " AND st.discount_id IN (SELECT discount_id FROM discounts WHERE discount_code = %s)"
            params.append(discount_code)
        if min_amount:
            query += " AND st.total >= %s"
            params.append(min_amount)
        if max_amount:
            query += " AND st.total <= %s"
            params.append(max_amount)

        query += " ORDER BY st.transaction_date, st.transaction_id, ti.transaction_item_id"

        cursor.execute(query, tuple(params))
        sales_data = cursor.fetchall()

        # Get daily sales
        daily_sales_query = """
            SELECT 
                DATE(st.transaction_date) AS sale_date, 
                SUM(st.total) AS total_sales,
                SUM(ti.quantity) AS total_quantity
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_date
            ORDER BY sale_date
        """
        cursor.execute(daily_sales_query, (start_date, end_date))
        daily_sales = cursor.fetchall()

        # Get monthly sales
        monthly_sales_query = """
            SELECT 
                DATE_FORMAT(st.transaction_date, '%Y-%M') AS sale_month, 
                SUM(st.total) AS total_sales,
                SUM(ti.quantity) AS total_quantity
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_month
            ORDER BY sale_month
        """
        cursor.execute(monthly_sales_query, (start_date, end_date))
        monthly_sales = cursor.fetchall()

        # Get yearly sales
        yearly_sales_query = """
            SELECT 
                YEAR(st.transaction_date) AS sale_year, 
                SUM(st.total) AS total_sales,
                SUM(ti.quantity) AS total_quantity
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_year
            ORDER BY sale_year
        """
        cursor.execute(yearly_sales_query, (start_date, end_date))
        yearly_sales = cursor.fetchall()

        # Get product variant sales
        variant_sales_query = """
            SELECT 
                pv.variant_id,
                pv.size_id,
                pv.color,
                SUM(ti.quantity) AS total_quantity
            FROM transaction_items ti
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            WHERE ti.transaction_id IN (
                SELECT transaction_id
                FROM sales_transactions
                WHERE transaction_date BETWEEN %s AND %s
            )
            GROUP BY pv.variant_id, pv.size_id, pv.color
            ORDER BY pv.variant_id
        """
        cursor.execute(variant_sales_query, (start_date, end_date))
        variant_sales = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify(
            sales_report=sales_data, 
            daily_sales=daily_sales,
            monthly_sales=monthly_sales,
            yearly_sales=yearly_sales,
            variant_sales=variant_sales
        ), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/inventory_report', methods=['GET'])
def inventory_report():
    try:
        # Get the filter parameters from query parameters
        start_date = request.args.get('start_date')
        end_date = request.args.get('end_date')
        
        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date_obj = datetime.strptime(start_date, date_format)
            end_date_obj = datetime.strptime(end_date, date_format)
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Daily sales report
        daily_sales_query = """
            SELECT 
                DATE(st.transaction_date) AS sale_date,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                SUM(ti.quantity) AS total_quantity_sold,
                SUM(ti.quantity * ti.price) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_date, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_date;
        """

        cursor.execute(daily_sales_query, (start_date, end_date))
        daily_sales = cursor.fetchall()

        # Weekly sales report
        weekly_sales_query = """
            SELECT 
                YEAR(st.transaction_date) AS sale_year,
                WEEK(st.transaction_date) AS sale_week,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                SUM(ti.quantity) AS total_quantity_sold,
                SUM(ti.quantity * ti.price) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_year, sale_week, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_year, sale_week;
        """

        cursor.execute(weekly_sales_query, (start_date, end_date))
        weekly_sales = cursor.fetchall()

        # Monthly sales report
        monthly_sales_query = """
            SELECT 
                YEAR(st.transaction_date) AS sale_year,
                MONTH(st.transaction_date) AS sale_month,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                SUM(ti.quantity) AS total_quantity_sold,
                SUM(ti.quantity * ti.price) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_year, sale_month, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_year, sale_month;
        """

        cursor.execute(monthly_sales_query, (start_date, end_date))
        monthly_sales = cursor.fetchall()

        # Yearly sales report
        yearly_sales_query = """
            SELECT 
                YEAR(st.transaction_date) AS sale_year,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                SUM(ti.quantity) AS total_quantity_sold,
                SUM(ti.quantity * ti.price) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_year, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_year;
        """

        cursor.execute(yearly_sales_query, (start_date, end_date))
        yearly_sales = cursor.fetchall()

        # Inventory Metrics
        inventory_metrics_query = """
            SELECT 
                COUNT(DISTINCT p.product_id) AS total_products,
                COUNT(DISTINCT p.category_id) AS total_categories,
                SUM(pv.quantity * pv.price) AS total_stock_value,
                COUNT(*) AS reorder_level_alert
            FROM productvariant pv
            JOIN product p ON pv.product_id = p.product_id
            WHERE pv.quantity <= pv.min_quantity
        """
        
        cursor.execute(inventory_metrics_query)
        inventory_metrics = cursor.fetchone()

        cursor.close()
        connection.close()

        return jsonify(
            daily_sales=daily_sales,
            weekly_sales=weekly_sales,
            monthly_sales=monthly_sales,
            yearly_sales=yearly_sales,
            inventory_metrics=inventory_metrics
        ), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500


import calendar
@app.route('/finance_report', methods=['GET'])
def finance_report():
    try:
        # Get the filter parameters from query parameters
        start_date_str = request.args.get('start_date')
        end_date_str = request.args.get('end_date')

        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date = datetime.strptime(start_date_str, date_format).date()
            end_date = datetime.strptime(end_date_str, date_format).date()
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Expenses query
        expenses_query = """
            SELECT 
                restock_date,
                SUM(cost) AS total_expenses
            FROM restock
            WHERE restock_date BETWEEN %s AND %s
            GROUP BY restock_date;
        """

        cursor.execute(expenses_query, (start_date, end_date))
        expenses_results = cursor.fetchall()

        # Monthly sales query
        monthly_sales_query = """
            SELECT 
                DATE_FORMAT(st.transaction_date, '%Y-%m') AS sale_month,
                SUM(ti.quantity * ti.price) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_month;
        """

        cursor.execute(monthly_sales_query, (start_date, end_date))
        monthly_sales_results = cursor.fetchall()

        # Yearly sales query
        yearly_sales_query = """
            SELECT 
                DATE_FORMAT(st.transaction_date, '%Y') AS sale_year,
                SUM(ti.quantity * ti.price) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_year;
        """

        cursor.execute(yearly_sales_query, (start_date, end_date))
        yearly_sales_results = cursor.fetchall()

        # Calculate monthly profit and loss
        monthly_profit_loss_data = []

        for month in monthly_sales_results:
            month_total_sales = month['total_sales']
            month_total_expenses = sum(expense['total_expenses'] for expense in expenses_results
                                       if month['sale_month'] in expense['restock_date'].strftime('%Y-%m'))

            month_profit_loss = month_total_sales - month_total_expenses
            month_number = int(month['sale_month'].split('-')[1])
            month_name = calendar.month_name[month_number]

            monthly_profit_loss_data.append({
                'sale_month': month_name,
                'year': int(month['sale_month'].split('-')[0]),
                'month': int(month['sale_month'].split('-')[1]),
                'total_sales': month_total_sales,
                'total_expenses': month_total_expenses,
                'profit_loss': month_profit_loss
            })

        # Calculate yearly profit and loss
        yearly_profit_loss_data = []

        for year in yearly_sales_results:
            year_total_sales = year['total_sales']
            year_total_expenses = sum(expense['total_expenses'] for expense in expenses_results
                                      if year['sale_year'] in expense['restock_date'].strftime('%Y'))

            year_profit_loss = year_total_sales - year_total_expenses

            yearly_profit_loss_data.append({
                'sale_year': year['sale_year'],
                'year': int(year['sale_year']),
                'total_sales': year_total_sales,
                'total_expenses': year_total_expenses,
                'profit_loss': year_profit_loss
            })

        cursor.close()
        connection.close()

        return jsonify({
            'monthly_profit_loss_report': monthly_profit_loss_data,
            'yearly_profit_loss_report': yearly_profit_loss_data
        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500



def create_inventory_views(start_date, end_date):
    connection = get_db_connection()
    cursor = connection.cursor(dictionary=True)

    try:
        # Drop existing views if they exist
        drop_view_query = "DROP VIEW IF EXISTS beginning_inventory_view, ending_inventory_view, daily_sales_view, weekly_sales_view, monthly_sales_view, yearly_sales_view, inventory_metrics_view;"
        cursor.execute(drop_view_query)

        # Create beginning inventory view
        create_beginning_inventory_view = f"""
            CREATE VIEW beginning_inventory_view AS
            SELECT 
                r.variant_id,
                pv.variant_id AS product_id,
                SUM(r.quantity) - COALESCE(SUM(ti.quantity), 0) AS beginning_inventory
            FROM restock r
            LEFT JOIN (
                SELECT ti.product_id, pv.variant_id, SUM(ti.quantity) AS quantity
                FROM transaction_items ti
                JOIN productvariant pv ON ti.product_id = pv.variant_id
                JOIN sales_transactions st ON ti.transaction_id = st.transaction_id
                WHERE st.transaction_date < '{start_date}'
                GROUP BY ti.product_id, pv.variant_id
            ) ti ON r.variant_id = ti.product_id AND r.variant_id = ti.variant_id
            JOIN productvariant pv ON r.variant_id = pv.variant_id
            WHERE r.restock_date < '{start_date}'
            GROUP BY r.variant_id, pv.variant_id;
        """
        cursor.execute(create_beginning_inventory_view)

        # Create ending inventory view
        create_ending_inventory_view = f"""
            CREATE VIEW ending_inventory_view AS
            SELECT 
                pv.product_id,
                pv.variant_id,
                SUM(pv.quantity) AS ending_inventory
            FROM productvariant pv
            WHERE pv.date_added <= '{end_date}'
            GROUP BY pv.product_id, pv.variant_id;
        """
        cursor.execute(create_ending_inventory_view)

        # Create daily sales view
        create_daily_sales_view = f"""
            CREATE VIEW daily_sales_view AS
            SELECT 
                DATE(st.transaction_date) AS sale_date,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                COALESCE(SUM(ti.quantity),0) AS total_quantity_sold,
                COALESCE(SUM(ti.quantity * ti.price),0) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN '{start_date}' AND '{end_date}'
            GROUP BY sale_date, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_date;
        """
        cursor.execute(create_daily_sales_view)

        # Create weekly sales view
        create_weekly_sales_view = f"""
            CREATE VIEW weekly_sales_view AS
            SELECT 
                YEAR(st.transaction_date) AS sale_year,
                WEEK(st.transaction_date) AS sale_week,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                COALESCE(SUM(ti.quantity),0) AS total_quantity_sold,
                COALESCE(SUM(ti.quantity * ti.price),0) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN '{start_date}' AND '{end_date}'
            GROUP BY sale_year, sale_week, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_year, sale_week;
        """
        cursor.execute(create_weekly_sales_view)

        # Create monthly sales view
        create_monthly_sales_view = f"""
            CREATE VIEW monthly_sales_view AS
            SELECT 
                YEAR(st.transaction_date) AS sale_year,
                MONTH(st.transaction_date) AS sale_month,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                COALESCE(SUM(ti.quantity),0) AS total_quantity_sold,
                COALESCE(SUM(ti.quantity * ti.price),0) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN '{start_date}' AND '{end_date}'
            GROUP BY sale_year, sale_month, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_year, sale_month;
        """
        cursor.execute(create_monthly_sales_view)

        create_yearly_sales_view = f"""
            CREATE VIEW yearly_sales_view AS
            SELECT 
                YEAR(st.transaction_date) AS sale_year,
                p.product_id,
                p.name AS product_name,
                pv.variant_id,
                pv.size_id,
                pv.color,
                COALESCE(SUM(ti.quantity),0) AS total_quantity_sold,
                COALESCE(SUM(ti.quantity * ti.price),0) AS total_sales
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN '{start_date}' AND '{end_date}'
            GROUP BY sale_year, p.product_id, pv.variant_id, pv.size_id, pv.color
            ORDER BY sale_year;
        """
        cursor.execute(create_yearly_sales_view)

        connection.commit()

    except Exception as e:
        print(f"Error creating views: {str(e)}")
        connection.rollback()

    finally:
        cursor.close()
        connection.close()
        
@app.route('/inventory_report_test', methods=['GET'])
def inventory_report_test():
    try:
        # Get the filter parameters from query parameters
        start_date = request.args.get('start_date')
        end_date = request.args.get('end_date')
        
        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date_obj = datetime.strptime(start_date, date_format)
            end_date_obj = datetime.strptime(end_date, date_format)
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        # Create views for beginning and ending inventory and daily, weekly, monthly sales
        create_inventory_views(start_date, end_date)

        # Now fetch the data using these views and calculate metrics
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Calculate average inventory level, turnover rate, and sell-through rate
        inventory_metrics_query = """
            CREATE VIEW inventory_metrics_view AS
            SELECT
                bi.product_id,
                bi.variant_id,
                (bi.beginning_inventory + ei.ending_inventory)/2 AS avg_inventory_level,
                (COALESCE(SUM(ds.total_quantity_sold),0)/ ((bi.beginning_inventory + ei.ending_inventory)/2)) * 100 AS turnover_rate,
                (COALESCE(SUM(ds.total_quantity_sold),0) / bi.beginning_inventory) * 100 AS sell_through_rate
            FROM beginning_inventory_view bi
            JOIN ending_inventory_view ei ON bi.variant_id = ei.variant_id
            LEFT JOIN daily_sales_view ds ON bi.variant_id = ds.variant_id
            GROUP BY bi.product_id, bi.variant_id;
        """
        cursor.execute(inventory_metrics_query)

        # Fetch the results from inventory_metrics_view
        inventory_metrics_query = """
            SELECT
                product_id,
                variant_id,
                avg_inventory_level,
                turnover_rate,
                sell_through_rate
            FROM inventory_metrics_view;
        """
        cursor.execute(inventory_metrics_query)
        inventory_metrics = cursor.fetchall()

        # Fetch daily sales data
        cursor.execute("SELECT * FROM daily_sales_view;")
        daily_sales = cursor.fetchall()

        # Fetch weekly sales data
        cursor.execute("SELECT * FROM weekly_sales_view;")
        weekly_sales = cursor.fetchall()

        # Fetch monthly sales data
        cursor.execute("SELECT * FROM monthly_sales_view;")
        monthly_sales = cursor.fetchall()

        # Fetch yearly sales data
        cursor.execute("SELECT * FROM yearly_sales_view;")
        yearly_sales = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify(
            inventory_metrics=inventory_metrics,
            daily_sales=daily_sales,
            weekly_sales=weekly_sales,
            monthly_sales=monthly_sales,
            yearly_sales=yearly_sales
        ), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500
















@app.route('/inventor2y_report', methods=['GET'])
def inventory_repor():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Query to fetch product and variant details
        query = """
            SELECT 
                p.product_id,
                p.name AS product_name,
                p.category_id,
                p.brand_id,
                p.price AS base_price,
                p.gender,
                p.date_added AS product_date_added,
                pv.variant_id,
                pv.color,
                pv.size_id,
                pv.quantity AS available_quantity,
                pv.price AS variant_price,
                pv.date_added AS variant_date_added
            FROM product p
            JOIN productvariant pv ON p.product_id = pv.product_id
        """
        cursor.execute(query)
        inventory_data = cursor.fetchall()

        # Calculate total inventory value and average inventory value
        total_inventory_value = sum(item['available_quantity'] * item['variant_price'] for item in inventory_data)
        average_inventory_value = total_inventory_value / len(inventory_data)

        # Calculate turnover rate and sell-through rate
        turnover_rate = calculate_turnover_rate(cursor)
        sell_through_rate = calculate_sell_through_rate(cursor, total_inventory_value)

        # Get top selling products
        top_selling_products = get_top_selling_products(cursor)

        # Get slow moving products
        slow_moving_products = get_slow_moving_products(cursor)

        # Get stock levels by category and brand
        stock_levels = get_stock_levels(cursor)

        cursor.close()
        connection.close()

        return jsonify({
            "inventory_data": inventory_data,
            "total_inventory_value": round(total_inventory_value, 2),
            "average_inventory_value": round(average_inventory_value,2),
            "turnover_rate": round(turnover_rate,2),
            "sell_through_rate": round(sell_through_rate,2),
            "top_selling_products": top_selling_products,
            "slow_moving_products": slow_moving_products,
            "stock_levels": stock_levels
        }), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

def calculate_turnover_rate(cursor):
    turnover_rate_query = """
        SELECT 
            (COUNT(DISTINCT st.transaction_id) / COUNT(DISTINCT st.transaction_date)) AS turnover_rate
        FROM sales_transactions st
    """
    cursor.execute(turnover_rate_query)
    result = cursor.fetchone()
    return result['turnover_rate']

def calculate_sell_through_rate(cursor, total_inventory_value):
    sell_through_rate_query = """
        SELECT 
            SUM(ti.quantity * ti.price) / %s AS sell_through_rate
        FROM transaction_items ti
        JOIN sales_transactions st ON ti.transaction_id = st.transaction_id
    """
    cursor.execute(sell_through_rate_query, (total_inventory_value,))
    result = cursor.fetchone()
    return result['sell_through_rate']

def get_top_selling_products(cursor):
    top_selling_products_query = """
        SELECT 
            p.product_id,
            p.name AS product_name,
            SUM(ti.quantity) AS total_quantity_sold,
            SUM(ti.quantity * ti.price) AS total_revenue
        FROM product p
        JOIN productvariant pv ON p.product_id = pv.product_id
        JOIN transaction_items ti ON pv.variant_id = ti.product_id
        JOIN sales_transactions st ON ti.transaction_id = st.transaction_id
        GROUP BY p.product_id
        ORDER BY total_quantity_sold DESC
        LIMIT 10
    """
    cursor.execute(top_selling_products_query)
    top_selling_products = cursor.fetchall()
    return top_selling_products

def get_slow_moving_products(cursor):
    slow_moving_products_query = """
        SELECT 
            p.product_id,
            p.name AS product_name,
            SUM(ti.quantity) AS total_quantity_sold,
            SUM(ti.quantity * ti.price) AS total_revenue
        FROM product p
        JOIN productvariant pv ON p.product_id = pv.product_id
        JOIN transaction_items ti ON pv.variant_id = ti.product_id
        JOIN sales_transactions st ON ti.transaction_id = st.transaction_id
        GROUP BY p.product_id
        ORDER BY total_quantity_sold ASC
        LIMIT 10
    """
    cursor.execute(slow_moving_products_query)
    slow_moving_products = cursor.fetchall()
    return slow_moving_products

def get_stock_levels(cursor):
    stock_levels_query = """
        SELECT 
            p.category_id,
            p.brand_id,
            COUNT(pv.variant_id) AS total_variants,
            SUM(pv.quantity) AS total_quantity
        FROM product p
        JOIN productvariant pv ON p.product_id = pv.product_id
        GROUP BY p.category_id, p.brand_id
    """
    cursor.execute(stock_levels_query)
    stock_levels = cursor.fetchall()
    return stock_levels


@app.route('/inventor2y_report', methods=['GET'])
def inventory_rep2ort():
    try:
        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Query to get inventory details
        inventory_query = """
            SELECT 
                p.product_id,
                p.name AS product_name,
                p.category_id,
                p.brand_id,
                pv.variant_id,
                pv.color,
                pv.size_id,
                pv.quantity AS available_quantity,
                pv.price AS variant_price,
                p.price AS base_price,
                p.gender,
                p.date_added AS product_added_date,
                pv.date_added AS variant_added_date
            FROM product p
            JOIN productvariant pv ON p.product_id = pv.product_id
        """
        cursor.execute(inventory_query)
        inventory_data = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify(inventory_report=inventory_data), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500





@app.route('/sales3_report', methods=['GET'])
def sales_repor3t():
    try:
        # Get the filter parameters from query parameters
        start_date = request.args.get('start_date')
        end_date = request.args.get('end_date')
        employee_id = request.args.get('employee_id')
        customer_id = request.args.get('customer_id')
        payment_method = request.args.get('payment_method')
        product_id = request.args.get('product_id')
        category_id = request.args.get('category_id')
        brand_id = request.args.get('brand_id')
        discount_code = request.args.get('discount_code')
        min_amount = request.args.get('min_amount')
        max_amount = request.args.get('max_amount')

        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date_obj = datetime.strptime(start_date, date_format)
            end_date_obj = datetime.strptime(end_date, date_format)
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Base query with dynamic filters
        query = """
            SELECT 
                st.transaction_id,
                st.transaction_date,
                st.employee_id,
                st.customer_id,
                st.total AS transaction_total,
                st.payment_method,
                st.discount_id AS transaction_discount_id,
                ti.transaction_item_id,
                ti.product_id,
                ti.quantity,
                ti.price AS item_price,
                ti.discount_id AS item_discount_id,
                p.name AS product_name,
                p.category_id,
                p.brand_id,
                pv.variant_id,
                pv.size_id,
                pv.color
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
        """
        params = [start_date, end_date]

        # Add filters dynamically
        if employee_id:
            query += " AND st.employee_id = %s"
            params.append(employee_id)
        if customer_id:
            query += " AND st.customer_id = %s"
            params.append(customer_id)
        if payment_method:
            query += " AND st.payment_method = %s"
            params.append(payment_method)
        if product_id:
            query += " AND ti.product_id = %s"
            params.append(product_id)
        if category_id:
            query += " AND p.category_id = %s"
            params.append(category_id)
        if brand_id:
            query += " AND p.brand_id = %s"
            params.append(brand_id)
        if discount_code:
            query += " AND st.discount_id IN (SELECT discount_id FROM discounts WHERE discount_code = %s)"
            params.append(discount_code)
        if min_amount:
            query += " AND st.total >= %s"
            params.append(min_amount)
        if max_amount:
            query += " AND st.total <= %s"
            params.append(max_amount)

        query += " ORDER BY st.transaction_date, st.transaction_id, ti.transaction_item_id"

        cursor.execute(query, tuple(params))
        sales_data = cursor.fetchall()

        # Get daily sales
        daily_sales_query = """
            SELECT 
                DATE(st.transaction_date) AS sale_date, 
                SUM(st.total) AS total_sales,
                SUM(ti.quantity) AS total_quantity
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_date
            ORDER BY sale_date
        """
        cursor.execute(daily_sales_query, (start_date, end_date))
        daily_sales = cursor.fetchall()

        # Get monthly sales
        monthly_sales_query = """
            SELECT 
                DATE_FORMAT(st.transaction_date, '%%Y-%%m') AS sale_month, 
                SUM(st.total) AS total_sales,
                SUM(ti.quantity) AS total_quantity
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_month
            ORDER BY sale_month
        """
        cursor.execute(monthly_sales_query, (start_date, end_date))
        monthly_sales = cursor.fetchall()

        # Get yearly sales
        yearly_sales_query = """
            SELECT 
                YEAR(st.transaction_date) AS sale_year, 
                SUM(st.total) AS total_sales,
                SUM(ti.quantity) AS total_quantity
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            WHERE st.transaction_date BETWEEN %s AND %s
            GROUP BY sale_year
            ORDER BY sale_year
        """
        cursor.execute(yearly_sales_query, (start_date, end_date))
        yearly_sales = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify(
            sales_report=sales_data, 
            daily_sales=daily_sales,
            monthly_sales=monthly_sales,
            yearly_sales=yearly_sales
        ), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500

      
  
@app.route('/sales_report2', methods=['GET'])
def sales_report2():
    try:
        # Get the filter parameters from query parameters
        start_date = request.args.get('start_date')
        end_date = request.args.get('end_date')
        employee_id = request.args.get('employee_id')
        customer_id = request.args.get('customer_id')
        payment_method = request.args.get('payment_method')
        product_id = request.args.get('product_id')
        category_id = request.args.get('category_id')
        brand_id = request.args.get('brand_id')
        discount_code = request.args.get('discount_code')
        min_amount = request.args.get('min_amount')
        max_amount = request.args.get('max_amount')

        # Validate date format
        date_format = "%Y-%m-%d"
        try:
            start_date_obj = datetime.strptime(start_date, date_format)
            end_date_obj = datetime.strptime(end_date, date_format)
        except ValueError:
            return jsonify({"error": "Invalid date format. Use YYYY-MM-DD."}), 400

        connection = get_db_connection()
        cursor = connection.cursor(dictionary=True)

        # Build the base query with dynamic filters
        query = """
            SELECT 
                st.transaction_id,
                st.transaction_date,
                st.employee_id,
                st.customer_id,
                st.total AS transaction_total,
                st.payment_method,
                st.discount_id AS transaction_discount_id,
                ti.transaction_item_id,
                ti.product_id,
                ti.quantity,
                ti.price AS item_price,
                ti.discount_id AS item_discount_id,
                p.name AS product_name,
                p.category_id,
                p.brand_id,
                pv.variant_id,
                pv.size_id,
                pv.color
            FROM sales_transactions st
            JOIN transaction_items ti ON st.transaction_id = ti.transaction_id
            JOIN productvariant pv ON ti.product_id = pv.variant_id
            JOIN product p ON pv.product_id = p.product_id
            WHERE st.transaction_date BETWEEN %s AND %s
        """
        params = [start_date, end_date]

        # Add filters dynamically
        if employee_id:
            query += " AND st.employee_id = %s"
            params.append(employee_id)
        if customer_id:
            query += " AND st.customer_id = %s"
            params.append(customer_id)
        if payment_method:
            query += " AND st.payment_method = %s"
            params.append(payment_method)
        if product_id:
            query += " AND ti.product_id = %s"
            params.append(product_id)
        if category_id:
            query += " AND p.category_id = %s"
            params.append(category_id)
        if brand_id:
            query += " AND p.brand_id = %s"
            params.append(brand_id)
        if discount_code:
            query += " AND st.discount_id IN (SELECT discount_id FROM discounts WHERE discount_code = %s)"
            params.append(discount_code)
        if min_amount:
            query += " AND st.total >= %s"
            params.append(min_amount)
        if max_amount:
            query += " AND st.total <= %s"
            params.append(max_amount)

        query += " ORDER BY st.transaction_date, st.transaction_id, ti.transaction_item_id"

        cursor.execute(query, tuple(params))
        sales_data = cursor.fetchall()

        cursor.close()
        connection.close()

        return jsonify(sales_report=sales_data), 200

    except Exception as e:
        return jsonify({"error": str(e)}), 500








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


if __name__ == '__main__':
    app.run(debug=True)
