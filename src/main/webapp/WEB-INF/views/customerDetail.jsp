<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>eLaundry System</title>
	<link rel="stylesheet"	href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">	
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/mainLayout.css"/>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/controls.css"/>
	
</head>
<body class="body-container">
	<div class="header-container">
		<div style="float:left;">
			<a href="<%=request.getContextPath()%>"><img src="./resources/images/ATeamLogo.png" alt="The A Team" /></a>
			<div class="banner-message" style="float:right;"><h1><spring:message code="welcome.message"/></h1></div>
		</div>
	</div>
	<div class="menu-container">
		<security:authorize access="isAnonymous()">
  			<a href="<spring:url value='/login' />" class="btn btn-default pull-right">Login</a>
		</security:authorize>
		<security:authorize access="isAuthenticated()">
			<security:authorize access="hasRole('ROLE_ADMIN')">
  				<a href="<spring:url value='/customers/add' />" class="btn btn-default"> 
					<span class="glyphicon-hand-left glyphicon"></span>Customer Signup
				</a>
				<a href="<spring:url value='/customers/list' />" class="btn btn-default"> 
					<span class="glyphicon-hand-left glyphicon"></span>Customers
				</a>
			</security:authorize>
			
			<a href="<spring:url value='/services/' />" class="btn btn-default">
				<span class="glyphicon-hand-left glyphicon"></span>Service
			</a>
			<a href="<spring:url value='/orders/placeOrder' />" class="btn btn-default">
				<span class="glyphicon-hand-left glyphicon"></span>Place an Order
			</a>
			<security:authorize access="hasRole('ROLE_ADMIN')">	
				<a href="<spring:url value='/orders/changeOrderStatus' />" class="btn btn-default">
					<span class="glyphicon-hand-left glyphicon"></span>Orders Placed
				</a>
			</security:authorize>			
		</security:authorize>
		<div class="language-change">
			View in: <a href="?language=en">English</a>|<a href="?language=nl">Dutch</a>
		</div>
	</div>
	<div class="content-container">
		<div class="list-container-small">
			<div class="thumbnail">
				<div class="caption">
					<p><span class="bold">Name:</span> ${customer.firstName} ${customer.lastName}</p>
					<p><span class="bold">Email:</span> ${customer.email}</p>
					<p><span class="bold">User:</span> ${customer.user.username}</p>
					<p><span class="bold">Address:</span> ${customer.address.street} ${customer.address.city}</p>
					<p>${customer.address.state}, ${customer.address.zipCode}</p>
				</div>
			</div>
		</div>
	</div>
	<div class="footer-container">
		<p class="copyright-message">@Copyright: The A Team</p>
		<p class="contributor">Consequence of collaborative effort of: (1) Masbha Uddin Ahmed (2) Md Mosharaf Hossain and (3) Sharif Hasan Khan</p>
	</div>
</body>
</html>
