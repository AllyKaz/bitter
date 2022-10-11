

function likePill(id, userId) {
    fetch("/likePill", {
        method: "POST",

        body: JSON.stringify({
            id: id,
            pillId: userId,
        }),

        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    }).then( response => {
        if(response.ok) {
            let number = parseInt(document.getElementById("likesCount" + id).innerText)
            document.getElementById("likesCount" + id).innerText = "" + ++number;
        }
    })
}