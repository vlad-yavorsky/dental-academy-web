# Dental Academy

**Requirements:**

* Java 11
* PostgreSQL 12

**Installing:**

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
    <td colspan="3" style="text-align: center">Database - Localhost</td>
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
    <td colspan="3" style="text-align: center">Database - Heroku Test Environment</td>
</tr>
<tr>
    <td>DATABASE_URL</td>
    <td>(Usually added automatically)<br/>Database url in format:<br/>postgres://username:password@host:port/dbname  </td>
    <td></td>
</tr>
<tr>
    <td colspan="3" style="text-align: center">Payment options</td>
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
    <td colspan="3" style="text-align: center">Payment Providers Accounts Credentials</td>
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
</tbody>
</table>
