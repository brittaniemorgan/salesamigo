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

if __name__ == '__main__':
    app.run(debug=True)
