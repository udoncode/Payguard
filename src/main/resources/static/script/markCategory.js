document.addEventListener("DOMContentLoaded", function() {
	const pathname = window.location.pathname;
	
	const selectedElement = document.querySelector(".selected");
    if (selectedElement) {
        selectedElement.classList.remove("selected");
    }
	
	const titleEl = document.querySelector('header h3')
	
	if (pathname.includes("budget")) {
		    document.querySelector(".category--budget").classList.add("selected");
			titleEl.innerHTML = "예산 계획"
	}
	else if (pathname.includes("transaction")) {
		    document.querySelector(".category--transaction").classList.add("selected");
			titleEl.innerHTML = "지출 관리"
	}
	else if (pathname.includes("analysis")) {
		    document.querySelector(".category--analysis").classList.add("selected");
			titleEl.innerHTML = "지출 분석"
	}
})