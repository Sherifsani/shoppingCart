# Shopping Cart API

A RESTful API for managing a shopping cart with product catalog and coupon functionality. Built with Spring Boot and PostgreSQL.

## Features

- **Product Management**
  - Fetch all available products
  - Product details with name and price
  
- **Shopping Cart**
  - Add/remove items
  - Update quantities
  - Calculate cart total
  
- **Coupon System**
  - Apply coupon code "POWERLABSx" for 13.2% discount
  - Automatic total recalculation

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Containerization**: Docker
- **API Documentation**: Swagger/OpenAPI

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker (optional)
- PostgreSQL 14+

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/shopping-cart-api.git
cd shopping-cart-api
```

### 2. Database Setup

1. Create a PostgreSQL database:
   ```sql
   CREATE DATABASE shoppingdb;
   CREATE USER shoppinguser WITH PASSWORD 'your_secure_password';
   GRANT ALL PRIVILEGES ON DATABASE shoppingdb TO shoppinguser;
   ```

2. Update `application.properties` with your database credentials

### 3. Run with Maven

```bash
# Build the application
mvn clean package

# Run the application
java -jar target/shoppingCart-0.0.1-SNAPSHOT.jar
```

### 4. Run with Docker

```bash
# Build the Docker image
docker build -t shopping-cart .

# Run with Docker Compose
docker-compose up -d
```

## API Endpoints

### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID

### Cart
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart/{userId}/add` - Add item to cart
- `PUT /api/cart/{userId}/items/{itemId}` - Update cart item quantity
- `DELETE /api/cart/{userId}/items/{itemId}` - Remove item from cart
- `POST /api/cart/{userId}/apply-coupon/{couponCode}` - Apply coupon code

## Environment Variables

Create a `.env` file in the root directory:

```env
DB_URL=jdbc:postgresql://localhost:5432/shoppingdb
DB_USERNAME=shoppinguser
DB_PASSWORD=your_secure_password
SERVER_PORT=8080
```

## Testing

Run the test suite:

```bash
mvn test
```

## Deployment

### AWS Elastic Beanstalk

1. Package the application:
   ```bash
   mvn clean package
   ```

2. Create a ZIP file:
   ```bash
   zip -r shopping-cart.zip target/*.jar
   ```

3. Deploy to Elastic Beanstalk through AWS Console

### Docker Deployment

```bash
docker-compose up --build -d
```

## Project Structure

```
src/
├── main/
│   ├── java/com/shop/shoppingcart/
│   │   ├── controller/    # REST controllers
│   │   ├── model/         # JPA entities
│   │   ├── repository/    # Data access layer
│   │   ├── service/       # Business logic
│   │   └── dto/           # Data Transfer Objects
│   └── resources/
│       ├── products.json  # Sample products
│       └── application.properties
├── test/                  # Test files
```

## Contributing

1. Create a new branch
2. Make your changes
3. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Spring Framework
- PostgreSQL
- Maven
- Docker
