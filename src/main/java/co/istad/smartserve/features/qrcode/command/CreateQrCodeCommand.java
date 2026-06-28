package co.istad.smartserve.features.qrcode.command;

import co.istad.smartserve.features.qrcode.dto.QrCodeResponse;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class CreateQrCodeCommand implements QrCodeCommand<QrCodeResponse> {
    private final Supplier<QrCodeResponse> action;

    @Override
    public QrCodeResponse execute() {
        return action.get();
    }
}
