/* Mobile Navigation Menu */
let isMobileMenuOpen = false;

function toggleMobileMenu() {
    if (isMobileMenuOpen) {
        document.querySelector("header").style.height = "68px";
        document.getElementById("menu-close-icon").classList.add("invisible");
        document.getElementById("menu-open-icon").classList.remove("invisible");
        document.querySelectorAll("#mobile-header-menu>*").forEach(e => {
            e.style.display = "none";
            e.style.opacity = "0";
        })
    }
    else {
        document.querySelector("header").style.height = "110vh";
        document.getElementById("menu-open-icon").classList.add("invisible");
        document.getElementById("menu-close-icon").classList.remove("invisible");
        const menus = document.querySelectorAll("#mobile-header-menu>*")
        for (let i = 0; i < menus.length; i++) {
            menus[i].style.display = "";
            setTimeout(() => {
                menus[i].style.opacity = "1";
            }, i * 50); // 100ms 간격으로 실행
        }
    }
    isMobileMenuOpen = !isMobileMenuOpen;
}

function handleResize() {
    let currentWidth = window.innerWidth;
    if(currentWidth > 767) {
        if(isMobileMenuOpen) {
            toggleMobileMenu();
        }
    }
}

// Close menu when width increases
window.addEventListener("resize", handleResize);

/* Fade Effect */
const targets = document.querySelectorAll(".fade-effect");
const options = { root: null, threshold: 0.1, rootMargin: "-0px" };
const observer = new IntersectionObserver(function (entries, observer) {
    entries.forEach((entry) => {
        const container = entry.target;
        if (entry.isIntersecting) {
            container.classList.add("fade-in");
        } else {
            //container.classList.remove("fade-in");
        }
    });
}, options);

targets.forEach((target) => {
    observer.observe(target);
});
