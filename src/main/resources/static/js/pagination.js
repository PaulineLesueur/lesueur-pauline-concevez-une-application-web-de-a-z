document.addEventListener('DOMContentLoaded', function () {
    const transactions = document.querySelectorAll('#transactionList li');
    const paginationContainer = document.getElementById('pagination');
    const transactionsPerPage = 3;
    const totalPages = Math.ceil((transactions.length - 1) / transactionsPerPage);

    function showPage(page) {
        transactions.forEach((transaction, index) => {
            if (index === 0) {
                transaction.style.display = ''; // Always show the header
            } else {
                transaction.style.display = (index > (page - 1) * transactionsPerPage && index <= page * transactionsPerPage) ? '' : 'none';
            }
        });
    }

    function createPagination() {
        paginationContainer.innerHTML = '';

        // Previous button
        const prev = document.createElement('li');
        prev.textContent = '«';
        prev.addEventListener('click', () => {
            const currentPage = parseInt(paginationContainer.querySelector('.active').textContent);
            if (currentPage > 1) {
                showPage(currentPage - 1);
                updatePagination(currentPage - 1);
            }
        });
        paginationContainer.appendChild(prev);

        // Page buttons
        for (let i = 1; i <= totalPages; i++) {
            const page = document.createElement('li');
            page.textContent = i;
            page.addEventListener('click', (e) => {
                showPage(parseInt(e.target.textContent));
                updatePagination(parseInt(e.target.textContent));
            });
            paginationContainer.appendChild(page);
        }

        // Next button
        const next = document.createElement('li');
        next.textContent = '»';
        next.addEventListener('click', () => {
            const currentPage = parseInt(paginationContainer.querySelector('.active').textContent);
            if (currentPage < totalPages) {
                showPage(currentPage + 1);
                updatePagination(currentPage + 1);
            }
        });
        paginationContainer.appendChild(next);
    }

    function updatePagination(activePage) {
        paginationContainer.querySelectorAll('li').forEach((li, index) => {
            if (index === activePage) {
                li.classList.add('active');
            } else {
                li.classList.remove('active');
            }
        });
    }

    // Initialize
    createPagination();
    showPage(1);
    updatePagination(1);
});