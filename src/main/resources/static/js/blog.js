
function toggleCategoryMenu() {
    const categoryMenu = document.querySelector("#category-menu");
    const openIcon = document.querySelector("#c-open-icon");
    const closeIcon = document.querySelector("#c-close-icon");

    if (categoryMenu.classList.contains("invisible")) {
        categoryMenu.classList.remove("invisible");
        openIcon.classList.add("invisible");
        closeIcon.classList.remove("invisible");
    }
    else {
        categoryMenu.classList.add("invisible");
        closeIcon.classList.add("invisible");
        openIcon.classList.remove("invisible");
    }
}
