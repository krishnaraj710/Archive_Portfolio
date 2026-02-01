✨ Features
Live Stock Prices (Yahoo Finance API)

Live Crypto Prices (CoinGecko API - free, no key needed)

Portfolio Tracking (buyPrice, qty, currentPrice, P&L)

Buy/Sell Lifecycle (current → sellingPrice on sell)

Dashboard (LIVE prices + buyPrice/qty)

MySQL Persistent Storage

8 REST Endpoints (tested with Postman)

🛠 Tech Stack

Frontend: None (Pure Backend API)
Backend: Spring Boot 3.5.10 + JPA + Hibernate
Database: MySQL 8.0 (H2 for dev)
APIs: Yahoo Finance + CoinGecko (free)
Testing: Postman

🚀 Quick Start
1. Clone & Run
bash
git clone https://github.com/YOUR_USERNAME/Api_Assets.git
cd Api_Assets
mvn spring-boot:run
2. MySQL Setup (or use H2 for dev)
   

# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/assets_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update


3. Base URL

http://localhost:8080/api/assets
📋 API Endpoints (Postman Ready)
Method	Endpoint	Description	Sample Request
POST	/api/assets	Add asset (buyPrice/qty + LIVE price)	{"assetType":"STOCK","symbol":"AAPL","buyPrice":210,"qty":10}
POST	/api/assets/sell/1	Sell asset (qty=0, current→sellingPrice)	No body
GET	/api/assets	All assets (full details)	-
GET	/api/assets/stocks	Stocks only	-
GET	/api/assets/crypto	Crypto only	-
GET	/api/assets/stock/AAPL	Specific stock	-
GET	/api/assets/crypto/bitcoin	Specific crypto	-
GET	/api/assets/dashboard	Dashboard (buyPrice/qty + LIVE prices)	-



🧪 Postman Test Flow
bash
# 1. ADD STOCK
POST /api/assets
{
  "assetType": "STOCK", "symbol": "AAPL", "name": "Apple Inc",
  "buyPrice": 210.00, "qty": 10
}

# 2. ADD CRYPTO  
POST /api/assets
{
  "assetType": "CRYPTO", "symbol": "bitcoin", "name": "Bitcoin BTC",
  "buyPrice": 90000.00, "qty": 0.5
}

# 3. DASHBOARD (LIVE prices)
GET /api/assets/dashboard
# → buyPrice,qty from DB + currentPrice(LIVE API)

# 4. SELL
POST /api/assets/sell/1  # AAPL sold, qty=0
🗄 Database Schema (user_assets table)
Column	Type	Description
id	BIGINT PK	Unique ID
assetType	VARCHAR	STOCK/CRYPTO
symbol	VARCHAR	AAPL/bitcoin
buyPrice	DECIMAL	User buy price
qty	INTEGER	Quantity held
currentPrice	DECIMAL	LIVE API
sellingPrice	DECIMAL	Sell price (after sell)
sellingDate	TIMESTAMP	Sell date


💰 Sample Dashboard Response
json
[
  
    "id": 1,
    "type": "STOCK",
    "symbol": "AAPL",
    "name": "Apple Inc",
    "buyPrice": 210.00,     // From DB
    "qty": 10,              // From DB
    "currentPrice": 235.82, // LIVE Yahoo Finance
    "currentDate": "2026-02-01T18:20:00"  // LIVE
  
]

