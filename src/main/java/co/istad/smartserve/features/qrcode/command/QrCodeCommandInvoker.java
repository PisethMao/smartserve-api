package co.istad.smartserve.features.qrcode.command;

import org.springframework.stereotype.Component;

@Component
public class QrCodeCommandInvoker {
    public <T> T execute(QrCodeCommand<T> command) {
        return command.execute();
    }
}
