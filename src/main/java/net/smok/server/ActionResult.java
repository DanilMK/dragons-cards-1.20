package net.smok.server;

import net.minecraft.text.Text;

public record ActionResult(boolean success, Text message) {

    public ActionResult(boolean success, String message, Object... args) {
        this(success, Text.translatable(message, args));
    }
}
