(function (window) {
    let toggleFlag = [];
    let isRegisteredOnEditClickEvent = [];
    let isRegisteredOnCancelEditClickEvent = [];

    document.addEventListener("click", (event) => {
        const imageUploadSection = document.getElementById("image-upload");
        const imageDetach = document.getElementById("image-detach");
        const toggleEditDeletePopup = document.querySelectorAll(`[id^="toggle-edit-delete-popup"]`);
        if (imageUploadSection && imageUploadSection.contains(event.target)) {
            const imageFile = document.getElementById("image-file");
            imageFile.click();
        }
        if (imageDetach && (imageDetach.contains(event.target) || imageDetach === event.target)) {
            const imageFile = document.getElementById("image-file");
            const imageDisplay = document.getElementById("image-display");
            imageFile.value = "";
            imageDisplay.src = "";
            imageDetach.style.display = "none";
        }
        if (toggleEditDeletePopup && toggleEditDeletePopup.length > 0) {
            toggleEditDeletePopup.forEach((element, index) => {
                if (!toggleFlag[index]) {
                    toggleFlag.push(false);
                }
                if (!isRegisteredOnEditClickEvent[index]) {
                    isRegisteredOnEditClickEvent.push(false);
                }
                if (!isRegisteredOnCancelEditClickEvent) {
                    isRegisteredOnCancelEditClickEvent.push(false);
                }
                if (element && element.contains(event.target) || element === event.target) {
                    updateToggleEditDeletePopup(index);
                    if (!(isRegisteredOnEditClickEvent[index] || isRegisteredOnCancelEditClickEvent[index])) {
                        captureClickOnEditSpringt(index);
                        captureClickOnCancelEditSpringt(index);
                    }
                }
            });
        }
    });

    const imageFile = document.getElementById("image-file");
    if (imageFile) {
        imageFile.onchange = () => {
            const [file] = imageFile.files;
            if (file) {
                const imageDisplay = document.getElementById("image-display");
                const imageDetach = document.getElementById("image-detach");
                const submitSpringtter = document.getElementById("submit-springtter");
                imageDisplay.src = URL.createObjectURL(file);
                imageDetach.style.display = "block";
                submitSpringtter.removeAttribute("disabled");
            }
        };
    }

    document
        .getElementById("message-springtter")
        .addEventListener("input", (event) => {
            if (event.target.value !== "") {
                const submitSpringtter = document.getElementById("submit-springtter");
                submitSpringtter.removeAttribute("disabled");
            }
        });

    let updateToggleEditDeletePopup = (index) => {
        const editDeletePopup = document.querySelectorAll(`[id^="edit-delete-popup"]`);
        if (editDeletePopup && editDeletePopup.length > 0) {
            editDeletePopup.forEach((element, i) => {
                if (index === i) {
                    toggleFlag[index] = !toggleFlag[index];
                    if (toggleFlag[index]) {
                        element.style.display = "block";
                    } else {
                        element.style.display = "none";
                    }
                }
            });
        }
    };

    let captureClickOnEditSpringt = (index) => {
        const editSpringt = document.querySelectorAll(`[id^="edit-springt"]`);
        if (editSpringt && editSpringt.length > 0) {
            editSpringt.forEach((element, i) => {
                if (index === i) {
                    element.addEventListener("click", (event) => {
                        const id = element.id.split("-").pop();
                        updateToggleEditDeletePopup(index);
                        document.getElementById(`springt-content-${id}`).style.display = "none";
                        document.getElementById(`springt-editing-content-${id}`).style.display = "block";
                        document.getElementById(`springt-actions-${id}`).style.visibility = "hidden";
                        document.getElementById(`springt-edit-actions-${id}`).style.visibility = "visible";
                    });
                    isRegisteredOnEditClickEvent[index] = true;
                }
            });
        }
    }

    let captureClickOnCancelEditSpringt = (index) => {
        const cancelEditSpringt = document.querySelectorAll(`[id^="cancel-edit-springt"]`);
        if (cancelEditSpringt && cancelEditSpringt.length > 0) {
            cancelEditSpringt.forEach((element, i) => {
                if (index === i) {
                    element.addEventListener("click", (event) => {
                        const id = element.id.split("-").pop();
                        document.getElementById(`springt-content-${id}`).style.display = "block";
                        document.getElementById(`springt-editing-content-${id}`).style.display = "none";
                        document.getElementById(`springt-actions-${id}`).style.visibility = "visible";
                        document.getElementById(`springt-edit-actions-${id}`).style.visibility = "hidden";
                    });
                    isRegisteredOnCancelEditClickEvent[index] = true;
                }
            });
        }
    }

    let editSpringtter = async (id) => {
        const message = document.getElementById(`edit-field-${id}`).value;
        window.location = `http://localhost:8080/edit-springtter/${id}/${message}`;
    };
    window.editSpringtter = editSpringtter;
})(window);