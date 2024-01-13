console.log("Script imported");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};

function deleteContact(cID) {
	const fetchUrl = `/user/delete-contact/${cID}`;
	console.log("here", fetchUrl);
	const confirmation = confirm('Are you sure you want to delete this contact?');
	if (confirmation) {
		fetch(fetchUrl, {
			method: 'post',
		})
			.then(response => {
				if (response.ok) {
					alert('Contact deleted successfully!');
					window.location.href = '/user/show-contacts/0';
					// You can redirect or perform other actions as needed after successful deletion
				} else {
					throw new Error('Failed to delete contact');
				}
			})
			.catch(error => {
				alert(error.message);
				// Handle the error or display a message to the user
			});
	}
};

const search = () => {
	let query = $("#search-input").val();
	if (query == "") {
		$(".search-result").hide();
	} else {
		let url = `http://localhost:8000/search/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
//				 data = JSON.parse(data);
//				console.log(data);
				let text = `<div class='list-group'>`;
				data.forEach((contact) => {
					text += `<a href="/user/contact/${contact.contactId}" class='list-group-item list-group-item-action'> ${contact.name} </a>`;
				});
				text += `</div>`;
				$(".search-result").html(text);
				$(".search-result").show();
			});
	}
};
