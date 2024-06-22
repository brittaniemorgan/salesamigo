from . import db
from sqlalchemy import Column, Integer, String, ForeignKey, Numeric
from sqlalchemy.orm import relationship


class Customers(db.Model):
    __tablename__ = 'customers'

    customer_id = db.Column(db.Integer, primary_key=True)
    firstname = db.Column(db.String(50))
    lastname = db.Column(db.String(50))
    email = db.Column(db.String(255), unique=True)
    contact_number = db.Column(db.String(20))
    password = db.Column(db.String(255))
    address = db.Column(db.String(255))
    points_balance = db.Column(db.Integer)
    date_joined = db.Column(db.DateTime)

    rewards = db.relationship('Rewards', uselist=False, back_populates='customers')
    sales_transaction = db.relationship('SalesTransaction', back_populates='customers')

    def __init__(self, firstname, lastname, email, contact_number, password, address, points_balance=0, date_joined=None):
        self.firstname = firstname
        self.lastname = lastname
        self.email = email
        self.contact_number = contact_number
        self.password = password
        self.address = address
        self.points_balance = points_balance
        self.date_joined = date_joined

    def is_authenticated(self):
        return True

    def is_active(self):
        return True

    def is_anonymous(self):
        return False

    def get_id(self):
        try:
            return unicode(self.customer_id)
        except NameError:
            return str(self.customer_id)

    def __repr__(self):
        return '<Customer %r>' % self.firstname % self.lastname


class Product(db.Model):
    __tablename__ = 'product'
    
    product_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(255))
    category_id = db.Column(db.Integer, db.ForeignKey('category.category_id'))
    brand_id = db.Column(db.Integer, db.ForeignKey('brand.brand_id'))
    price = db.Column(db.Numeric(10, 2))
    description = db.Column(db.Text)
    gender = db.Column(db.Enum('Male', 'Female', 'Unisex'))
    date_added = db.Column(db.Date)

    category = db.relationship('Category', back_populates='product')
    brand = db.relationship('Brand', back_populates='product')
    productvariant = db.relationship('ProductVariant', back_populates='product')
    transaction_items = db.relationship('TransactionItems', back_populates='product')

    def __init__(self, name, category_id, brand_id, price, description, gender, date_added=None):
        self.name = name
        self.category_id = category_id
        self.brand_id = brand_id
        self.price = price
        self.description = description
        self.gender = gender
        self.date_added = date_added

    def __repr__(self):
        return f'<Product {self.name}>'


class Category(db.Model):
    __tablename__ = 'category'
    
    category_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    category = db.Column(db.String(50))
    
    product = db.relationship('Product', back_populates='category')

    def __init__(self, category):
        self.category = category

    def __repr__(self):
        return f'<Category {self.category}>'


class Brand(db.Model):
    __tablename__ = 'brand'
    
    brand_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    brand = db.Column(db.String(50))
    
    product = db.relationship('Product', back_populates='brand')

    def __init__(self, brand):
        self.brand = brand

    def __repr__(self):
        return f'<Brand {self.brand}>'
        

class ProductVariant(db.Model):
    __tablename__ = 'productvariant'
    
    variant_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    product_id = db.Column(db.Integer, db.ForeignKey('product.product_id'))
    size_id = db.Column(db.Integer, db.ForeignKey('size.size_id'))
    color = db.Column(db.String(50))
    quantity = db.Column(db.Integer)
    price = db.Column(db.Numeric(10, 2))
    min_quantity = db.Column(db.Integer)
    date_added = db.Column(db.Date)

    product = db.relationship('Product', back_populates='productvariant')
    size = db.relationship('Size', back_populates='productvariant')

    def __init__(self, product_id, size_id, color, quantity, price, min_quantity, date_added=None):
        self.product_id = product_id
        self.size_id = size_id
        self.color = color
        self.quantity = quantity
        self.price = price
        self.min_quantity = min_quantity
        self.date_added = date_added

    def __repr__(self):
        return f'<ProductVariant {self.variant_id}>'


class Size(db.Model):
    __tablename__ = 'size'
    
    size_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    size = db.Column(db.String(20))
    
    productvariant = db.relationship('ProductVariant', back_populates='size')

    def __init__(self, size):
        self.size = size

    def __repr__(self):
        return f'<Size {self.size}>'


class Rewards(db.Model):
    __tablename__ = 'rewards'

    rewards_id = Column(Integer, primary_key=True, autoincrement=True)
    customer_id = Column(Integer, ForeignKey('customers.customer_id'))
    firstname = Column(String(50))
    lastname = Column(String(50))
    email = Column(String(255))
    contact_number = Column(String(20))
    rewards_balance = Column(Integer, default=10)

    customers = relationship("Customers", back_populates="rewards")

    def __init__(self, customer_id, firstname, lastname, email, contact_number, rewards_balance=0):
        self.customer_id = customer_id
        self.firstname = firstname
        self.lastname = lastname
        self.email = email
        self.contact_number = contact_number
        self.rewards_balance = rewards_balance

    def __repr__(self):
        return f'<Rewards {self.rewards_id}: {self.firstname} {self.lastname}>'


class ProductInventory(db.Model):
    __tablename__ = 'product_inventory'

    product_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(255))
    description = db.Column(db.Text)
    price = db.Column(db.Numeric(10, 2))
    quantity = db.Column(db.Integer)
    maximum_quantity = db.Column(db.Integer)
    minimum_quantity = db.Column(db.Integer)
    date_added = db.Column(db.Date)
    last_date_updated = db.Column(db.TIMESTAMP)
    category = db.Column(db.String(255))

    def __init__(self, name, description, price, quantity, maximum_quantity, minimum_quantity, date_added, last_date_updated=None, category=None):
        self.name = name
        self.description = description
        self.price = price
        self.quantity = quantity
        self.maximum_quantity = maximum_quantity
        self.minimum_quantity = minimum_quantity
        self.date_added = date_added
        self.last_date_updated = last_date_updated
        self.category = category

    def __repr__(self):
        return f'<ProductInventory {self.product_id}: {self.name}>'


class SalesTransaction(db.Model):
    __tablename__ = 'sales_transaction'

    transaction_id = Column(db.Integer, primary_key=True, autoincrement=True)
    transaction_date = Column(db.DateTime)
    employee_id = Column(db.Integer, default=550)
    customer_id = Column(db.Integer, ForeignKey('customers.customer_id'))
    total = Column(db.Numeric(10, 2))
    payment_method = Column(db.String(50))
    discount_id = Column(db.Integer, nullable=True)

    customers = relationship('Customers', back_populates='sales_transaction')
    transaction_items = relationship('TransactionItems', back_populates='sales_transaction')

    def __init__(self, customer_id, total, payment_method, discount_id, transaction_date=None):
        self.customer_id = customer_id
        self.total = total
        self.payment_method = payment_method
        self.discount_id = discount_id
        self.transaction_date = transaction_date

    def __repr__(self):
        return f'<SalesTransaction {self.transaction_id}>'


class TransactionItems(db.Model):
    __tablename__ = 'transaction_items'

    item_id = Column(db.Integer, primary_key=True, autoincrement=True)
    transaction_id = Column(db.Integer, ForeignKey('sales_transaction.transaction_id'))
    product_id = Column(db.Integer, ForeignKey('product.product_id'))
    quantity = Column(db.Integer)
    price = Column(db.Numeric(10, 2))
    discount_id = Column(db.Integer, nullable=True)

    sales_transaction = relationship('SalesTransaction', back_populates='transaction_items')
    product = relationship('Product', back_populates='transaction_items')

    def __init__(self, transaction_id, product_id, quantity, price, discount_id=None):
        self.transaction_id = transaction_id
        self.product_id = product_id
        self.quantity = quantity
        self.price = price
        self.discount_id = discount_id

    def __repr__(self):
        return f'<TransactionItem {self.item_id}>'