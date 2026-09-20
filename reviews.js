const reviewParams = new URLSearchParams(window.location.search);
const reviewProductId = reviewParams.get("productId");

const reviewList = document.getElementById("reviewsList");
const reviewForm = document.getElementById("reviewForm");
const reviewMessage = document.getElementById("reviewMessage");

function reviewEscapeHtml(value) {
    const div = document.createElement("div");
    div.textContent = value ?? "";
    return div.innerHTML;
}

function loadProductReviews() {
    if (!reviewProductId || !reviewList) {
        return;
    }

    fetch("../api/reviews?productId=" + encodeURIComponent(reviewProductId))
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                reviewList.innerHTML =
                    "<p>" +
                    reviewEscapeHtml(
                        data.message || "Unable to load reviews."
                    ) +
                    "</p>";
                return;
            }

            const reviews = data.data || [];

            if (reviews.length === 0) {
                reviewList.innerHTML =
                    "<p>No reviews yet.</p>";
                return;
            }

            reviewList.innerHTML = reviews.map(review => `
                <div class="order-card">
                    <div class="order-header">
                        <strong>
                            ${reviewEscapeHtml(
                                review.userName || "Customer"
                            )}
                        </strong>

                        <span class="status-badge">
                            ${"★".repeat(Number(review.rating))}
                        </span>
                    </div>

                    <div class="order-details">
                        <p>
                            ${reviewEscapeHtml(review.comment || "")}
                        </p>

                        <small>
                            ${reviewEscapeHtml(review.createdAt || "")}
                        </small>
                    </div>
                </div>
            `).join("");
        })
        .catch(error => {
            console.error(error);
            reviewList.innerHTML =
                "<p>Unable to load reviews.</p>";
        });
}

if (reviewForm) {
    reviewForm.addEventListener("submit", function(event) {
        event.preventDefault();

        if (!reviewProductId) {
            reviewMessage.textContent =
                "Product ID is missing.";
            return;
        }

        const rating =
            Number(document.getElementById("rating").value);

        const comment =
            document.getElementById("comment").value.trim();

        if (rating < 1 || rating > 5 || !comment) {
            reviewMessage.textContent =
                "Please enter a rating and comment.";
            return;
        }

        fetch("../api/reviews?action=create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                productId: Number(reviewProductId),
                rating: rating,
                comment: comment
            })
        })
        .then(response => response.json())
        .then(data => {
            reviewMessage.textContent =
                data.message || "Review submitted.";

            if (data.success) {
                reviewForm.reset();
                loadProductReviews();
            }
        })
        .catch(error => {
            console.error(error);
            reviewMessage.textContent =
                "Unable to submit review.";
        });
    });
}

loadProductReviews();
