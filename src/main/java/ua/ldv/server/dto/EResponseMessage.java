package ua.ldv.server.dto;

public enum EResponseMessage {

    /**
     * Response message enum
     */

    REGISTER_SUCCESSFULLY("User registered successfully!"),
    CATEGORY_DELETED("Category deleted"),
    CATEGORY_IS_EMPTY("Category is empty"),
    POST_DELETED("Post deleted");

    private String message;

    EResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
