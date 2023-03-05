console.log("this is a script file");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		//true
		//band karna hai
		$(".sidebar").css("display", "none");
		$(".main").css("margin-left", "0%");
	} else {
		//false
		//show karna hai
		$(".sidebar").css("display", "block");
		$(".main").css("margin-left", "20%");
	}
};

const search = () => {
	let query = $("#search-input").val();

	if (query == "") {
		$(".search-result").hide();
	} else {
		//search
		console.log(query);
		//sending request to server
		let url = `http://localhost:8282/search/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				//data
				//console.log(data);
				let text = `<div class='list-group'>`;

				data.forEach((contact) => {
					text += `<a href='/user/contact/${contact.C_id}' class='list-group-item list-group-item-action'> ${contact.name} </a>`;
				});

				text += '</div>';

				$(".search-result").html(text);
				$(".search-result").show();
			});
	}
};

//first request to server to create order
const paymentstart = () => {
	console.log("payment started.....");
	let amount = $("#payment_field").val();
	console.log(amount);
	if (amount == "" || amount == null) {
		//alert("amount is required !!");
		swal("Failed !!", "amount is required !!", "error");
		return;
	}
};

//code...
//we will use ajax to send request to server to create order- jquery

$.ajax(
	{
		url: '/user/create_order',
		data: JSON.stringify({ amount: amount, info: 'order_request' }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response) {
			//invoked when success
			console.log(response);
			if (response.status == "created") {
				//open payment form
				let options = {
					key: "rzp_test_m8tHz3pZ8v2uj8",
					amount: response.amount,
					currency: "INR",
					name: "Contact Management System",
					description: "Your payment is secure here.",
					image: "data:image/jpeg;base64,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",
					order_id: response.id,
					handler: function(response) {
						console.log(response.razorpay_payment_id);
						console.log(response.razorpay_order_id);
						console.log(response.razorpay_signature);
						console.log("payment successfull !!");
						//alert("congrates !! Payment successfull !!);

						updatePaymentOnServer(
							response.razorpay_payment_id,
							response.razorpay_order_id,
							"paid"
						);
					},
					prefill: {
						name: "",
						email: "",
						contact: ""
					},
					notes: {
						address: "Abhinav Jha -Software Developer"
					},
					theme: {
						color: "#3399cc"
					},
				};
				let rzp = new Razorpay(options);
				rzp.on("payment.failed", function(response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					//alert("Oops payment failed !!");
				});
				rzp.open();
			}
		},
		error: function(error) {
			//invoked when error
			console.log(error);
			alert("something went wrong !!")
		}
	}
);

function updatePaymentOnServer(payment_id, order_id, status)
{
	$.ajax({
		url: '/user/update_order',
		data: JSON.stringify({ payment_id: payment_id,
		order_id: order_id,
		status: status }),
		contentType: 'application/json',
		type: 'POST',
		dataType: 'json',
		success: function(response) {
			swal("Good job!", "congrates !! Payment successful !!", "success");
		},
		error: function(error) {
			swal("Failed !!", "Oops payment failed !!", "error");
		 }
	});
}


/*You can see your payment by just
searching razorpay on google and go
on razorpay dashboard tap on home at
left cornor at the bottom you recent
payment or payment activities option*/

