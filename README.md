# Çalışan Yönetim Sistemi

Spring Boot 2.7.11 + Java 17 ile geliştirilmiş RESTful çalışan yönetim API'si.  
Çalışan kayıt, arama, listeleme, güncelleme ve silme işlemlerini destekler.

---

## Teknoloji Stack

| Katman | Teknoloji |
|---|---|
| Dil / JVM | Java 17 |
| Framework | Spring Boot 2.7.11 |
| Web | Spring Web (REST) |
| Güvenlik | Spring Security (Basic Auth) |
| Persistans | Spring Data JPA + Hibernate |
| DB (dev) | H2 (in-memory) |
| DB (prod) | Oracle (ojdbc8) |
| Kod üretimi | MapStruct, Lombok |
| API Dökümanı | springdoc-openapi (Swagger UI) |
| Build | Maven 3.x |
| Container | Docker (distroless Java 17) |

---

## Gereksinimler

- JDK 17+
- Maven 3.8+
- Docker (opsiyonel)

---

## Hızlı Başlangıç

```bash
# Kaynak kodu indir
git clone https://github.com/gurcangul/sturdy-eureka.git
cd sturdy-eureka

# Derleme (testleri atla)
mvn -DskipTests clean package

# Çalıştır (dev profili — H2 in-memory DB)
java -jar target/employee-management-1.0.0-SNAPSHOT.jar
```

Uygulama ayağa kalktıktan sonra:

| Adres | Açıklama |
|---|---|
| http://localhost:8080/swagger-ui/index.html | Swagger UI |
| http://localhost:8080/h2-console | H2 DB konsolu (dev) |
| http://localhost:8080/actuator/health | Sağlık kontrolü |

---

## Kimlik Doğrulama

Tüm API endpoint'leri **HTTP Basic Auth** gerektirir.

| Kullanıcı | Şifre | Rol |
|---|---|---|
| `admin` | `admin123` | ADMIN |
| `user` | `user123` | USER |

> Swagger UI üzerinden "Authorize" butonuna tıklayarak giriş yapabilirsiniz.

---

## API Endpoint'leri

Base URL: `/api/v1/employees`

### Çalışan İşlemleri

| Method | URL | Açıklama |
|---|---|---|
| `POST` | `/api/v1/employees` | Yeni çalışan kaydet |
| `GET` | `/api/v1/employees` | Tüm çalışanları listele (sayfalı) |
| `GET` | `/api/v1/employees/{id}` | ID ile çalışan detayı |
| `GET` | `/api/v1/employees/number/{employeeNumber}` | Sicil no ile çalışan detayı |
| `PUT` | `/api/v1/employees/{id}` | Çalışan bilgilerini güncelle |
| `DELETE` | `/api/v1/employees/{id}` | Çalışanı sil |
| `POST` | `/api/v1/employees/search` | Filtreli arama |

### Örnek: Çalışan Oluştur

```http
POST /api/v1/employees
Content-Type: application/json
Authorization: Basic YWRtaW46YWRtaW4xMjM=

{
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "email": "ahmet.yilmaz@sirket.com",
  "phone": "05551234567",
  "department": "Yazılım",
  "position": "Kıdemli Geliştirici",
  "salary": 25000.00,
  "startDate": "2024-01-15",
  "status": "ACTIVE"
}
```

### Örnek: Arama

```http
POST /api/v1/employees/search
Content-Type: application/json

{
  "firstName": "Ahmet",
  "department": "Yazılım",
  "status": "ACTIVE",
  "page": 0,
  "size": 10,
  "sortBy": "lastName",
  "sortDirection": "ASC"
}
```

### Örnek: Sayfalı Liste

```http
GET /api/v1/employees?page=0&size=20&sortBy=lastName&sortDirection=ASC
```

---

## Proje Yapısı

```
src/
├── main/
│   ├── java/com/example/employeemanagement/
│   │   ├── EmployeeManagementApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java          # Basic Auth, in-memory users
│   │   │   └── OpenApiConfig.java           # Swagger ayarları
│   │   ├── controller/
│   │   │   └── EmployeeController.java      # REST endpoint'leri
│   │   ├── service/
│   │   │   ├── EmployeeService.java         # Arayüz
│   │   │   └── impl/EmployeeServiceImpl.java
│   │   ├── repository/
│   │   │   └── EmployeeRepository.java      # JPA + custom @Query
│   │   ├── entity/
│   │   │   ├── Employee.java                # JPA entity + Auditing
│   │   │   └── EmployeeStatus.java          # ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
│   │   ├── dto/
│   │   │   ├── request/                     # CreateRequest, UpdateRequest, SearchRequest
│   │   │   └── response/                    # EmployeeDto, ApiResponse<T>, PageResponse<T>
│   │   ├── mapper/
│   │   │   └── EmployeeMapper.java          # MapStruct
│   │   └── exception/
│   │       ├── GlobalExceptionHandler.java
│   │       ├── EmployeeNotFoundException.java
│   │       └── DuplicateEmployeeException.java
│   └── resources/
│       ├── application.yml                  # Ortak ayarlar
│       ├── application-dev.yml              # H2 + debug logging
│       ├── application-prod.yml             # Oracle config
│       └── data.sql                         # Örnek veri (5 çalışan)
└── test/
    └── java/com/example/employeemanagement/
        ├── EmployeeManagementApplicationTests.java
        └── service/EmployeeServiceTest.java
```

---

## Veritabanı

### Dev Profili (Varsayılan)

H2 in-memory veritabanı kullanılır, uygulama her başladığında tablo otomatik oluşturulur ve `data.sql` ile 5 örnek çalışan eklenir.

H2 konsol bağlantısı:
- URL: `jdbc:h2:mem:employeedb`
- Kullanıcı: `sa`
- Şifre: *(boş)*

### Prod Profili

Oracle bağlantısı için ortam değişkenleri gereklidir:

```bash
export DB_URL=jdbc:oracle:thin:@localhost:1521/ORCL
export DB_USERNAME=your_user
export DB_PASSWORD=your_pass

java -Dspring.profiles.active=prod -jar target/employee-management-1.0.0-SNAPSHOT.jar
```

---

## Docker

```bash
# Image oluştur
mvn -DskipTests clean package
docker build \
  --build-arg ARTIFACT_NAME=employee-management \
  --build-arg ARTIFACT_VERSION=1.0.0-SNAPSHOT \
  --build-arg ARTIFACT_TYPE=jar \
  -t employee-management:1.0.0 .

# Çalıştır
docker run -p 8080:8080 \
  -e DB_URL=jdbc:oracle:thin:@db-host:1521/ORCL \
  -e DB_USERNAME=your_user \
  -e DB_PASSWORD=your_pass \
  employee-management:1.0.0
```

---

## Testler

```bash
# Tüm testleri çalıştır
mvn test

# Sadece birim testleri
mvn test -Dtest=EmployeeServiceTest
```

---

## Çalışan Durumları

| Durum | Açıklama |
|---|---|
| `ACTIVE` | Aktif çalışan |
| `INACTIVE` | Pasif çalışan |
| `ON_LEAVE` | İzinde |
| `TERMINATED` | İşten ayrılmış |
