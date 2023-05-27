# Dental Academy

### Requirements:

* Java 17
* PostgreSQL 15

### Default users:

| E-mail            | Password  |
|-------------------|-----------|
| admin@example.com | admin     |
| user@example.com  | user      |

### Installation:

Set next environment variables:

<table>
<thead>
<tr>
    <th>Environment Variable</th>
    <th>Description</th>
    <th>Example</th>
</tr>
</thead>
<tbody>
<tr>
    <td colspan="3" align="center"><strong><strong>Database - Localhost</strong></strong></td>
</tr>
<tr>
    <td>PORT</td>
    <td>Application port.<br/>Default: 8080</td>
    <td></td>
</tr>
<tr>
    <td>JDBC_DATABASE_URL</td>
    <td>Database url.<br/>Default: jdbc:postgresql://localhost:5432/dental-academy</td>
    <td></td>
</tr>
<tr>
    <td>JDBC_DATABASE_USERNAME</td>
    <td>Database username.<br/>Default: postgres</td>
    <td></td>
</tr>
<tr>
    <td>JDBC_DATABASE_PASSWORD</td>
    <td>Database password.<br/>Default: postgres</td>
    <td></td>
</tr>
<tr>
    <td colspan="3" align="center"><strong>Payment options</strong></td>
</tr>
<tr>
    <td>PAYMENT_PROVIDER</td>
    <td>Payment provider</td>
    <td>LIQPAY<br/>FONDY<br/>PORTMONE<br/>WAYFORPAY</td>
</tr>
<tr>
    <td>ORDER_PREFIX</td>
    <td>Order prefix. Will be added to the number of order.<br/>Solves the problem when several environments use the same payment provider account.<br/>Default: LOCAL-</td>
    <td>TEST-</td>
</tr>
<tr>
    <td>PAYMENT_TIME</td>
    <td>Time limit to purchase the order (in minutes).<br/>Default: 15</td>
    <td>30</td>
</tr>
<tr>
    <td>CURRENCY</td>
    <td>Currency for purchases.<br/>Default: UAH</td>
    <td>UAH<br/>USD</td>
</tr>
<tr>
    <td>CALLBACK_HOST</td>
    <td>Callback host for payment providers, to which the client will be redirected after purchase on the checkout page (without slash in the end)</td>
    <td>https://dental-academy.herokuapp.com</td>
</tr>
<tr>
    <td colspan="3" align="center"><strong>Payment Providers Accounts Credentials</strong></td>
</tr>
<tr>
    <td>LIQPAY_PRIVATE_KEY</td>
    <td>LiqPay Private Key</td>
    <td></td>
</tr>
<tr>
    <td>LIQPAY_PUBLIC_KEY</td>
    <td>LiqPay Public Key</td>
    <td></td>
</tr>
<tr>
    <td>FONDY_MERCHANT_ID</td>
    <td>Fondy Merchant Id</td>
    <td></td>
</tr>
<tr>
    <td>FONDY_MERCHANT_PASSWORD</td>
    <td>Fondy Merchant Password</td>
    <td></td>
</tr>
<tr>
    <td>PORTMONE_PAYEE_ID</td>
    <td>Portmone Payee Id</td>
    <td></td>
</tr>
<tr>
    <td>WAYFORPAY_LOGIN</td>
    <td>WayForPay Login</td>
    <td></td>
</tr>
<tr>
    <td>WAYFORPAY_SECRET_KEY</td>
    <td>WayForPay Secret Key</td>
    <td></td>
</tr>
<tr>
    <td colspan="3" align="center"><strong>AWS</strong></td>
</tr>
<tr>
    <td>AWS_ACCESS_KEY_ID</td>
    <td>AWS Access key id</td>
    <td></td>
</tr>
<tr>
    <td>AWS_SECRET_ACCESS_KEY</td>
    <td>AWS Secret access key</td>
    <td></td>
</tr>
<tr>
    <td>AWS_REGION</td>
    <td>AWS Region</td>
    <td>eu-central-1</td>
</tr>
<tr>
    <td>S3_BUCKET_NAME</td>
    <td>S3 Bucket Name</td>
    <td></td>
</tr>
<tr>
    <td colspan="3" align="center"><strong>etc</strong></td>
</tr>
<tr>
    <td>HOST</td>
    <td>Domain name used for emails (without slash in the end)</td>
    <td>http://dental-academy.herokuapp.com</td>
</tr>
<tr>
    <td>STORAGE_LOCATION</td>
    <td>Folder, where to save files uploaded via website</td>
    <td>C:/dental-academy/files</td>
</tr>
<tr>
    <td>NOTIFICATION_EMAILS</td>
    <td>Administrators Emails, where to send different notification about events on website (eg. user registered for event)</td>
    <td>admin@example.com</td>
</tr>
</tbody>
</table>
