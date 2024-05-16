from flask import Flask, render_template, request, redirect, url_for, jsonify, send_from_directory, abort
from flask_wtf.csrf import CSRFProtect
from flask_wtf.csrf import generate_csrf
import os
import mysql.connector

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
    database = 'SalesAmigo')
    return connection


@app.route('/')
def hello():
    data = {'message': 'Hello from API'}
    return jsonify(data)

"""do it like this?"""

@app.route('/log', methods=['POST'])
def test():
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
        cursor = connection.cursor()
        content = request.json
        username = content['username']
        password = content['password']
        cursor.execute('SELECT * FROM User WHERE username = %s AND password = %s', (username, password))
        user = cursor.fetchone()

        if user is not None:
            return jsonify({"message": "User logged in successfully",
                            "user":user}), 200
        else:
            return jsonify({"errors": "Invalid username or password"}), 400  

    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/users', methods=['GET'])
def get_users():
    try:
        connection = get_db_connection()
        cursor = connection.cursor()
        query = f"SELECT * FROM User"
        cursor.execute(query)
        result = cursor.fetchall()

        users = []
        for username, password, role in result:
            users.append({
                'username': username,
                'password': password,
                'role': role
            })
        cursor.close()
        connection.close()
        return jsonify(users=users) 

    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/csrf-token', methods=['GET']) 
def get_csrf(): 
    return jsonify({'csrf_token': generate_csrf()}) 