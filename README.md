# Floussi Merchant

An Android UI prototype for merchant payment and transaction workflows in Morocco.

The app explores a merchant-facing experience for receiving payments through QR codes, NFC, and phone-based flows, with transaction history and lightweight analytics.

> **Prototype:** this repository does not process real payments or connect to a production financial backend.

<!-- Add screenshots of Home, QR, NFC, Payment, and Analytics here. -->

## Prototype flows

- merchant home and balance overview;
- transaction history;
- manual payment entry;
- QR-code payment request;
- NFC payment interaction;
- phone-payment flow;
- success confirmation;
- analytics view;
- animated navigation and haptic feedback.

## Stack

- Kotlin
- Jetpack Compose
- Material 3
- Android Navigation Compose
- ZXing for QR codes
- Android NFC APIs
- Lottie and Coil

## Requirements

- Android Studio
- JDK 11+
- Android SDK 36
- Android device or emulator running API 24+

## Run

```bash
git clone https://github.com/OmarTaheri/floussimerchant.git
cd floussimerchant
./gradlew assembleDebug
```

Or open the repository in Android Studio and run the `app` configuration.

NFC behavior should be tested on a compatible physical device.

## Project structure

```text
app/src/main/java/com/example/floussi/
├── data/model/
├── ui/navigation/
├── ui/screens/
│   ├── analytics/
│   ├── home/
│   ├── nfc/
│   ├── payment/
│   ├── phonepayment/
│   ├── qr/
│   └── success/
└── util/
```

## Current limitations

- demonstration data only;
- no authentication or merchant onboarding;
- no backend, ledger, settlement, or reconciliation;
- no payment-provider integration;
- no security review;
- generated example tests only;
- placeholder Android namespace/application ID.

## Roadmap

- [ ] Replace `com.example.floussi` with a project namespace
- [ ] Remove IDE-local files from version control
- [ ] Add screenshots and a short demo video
- [ ] Add unit and Compose UI tests
- [ ] Define an API contract with a mock server
- [ ] Add secure local storage and session handling
- [ ] Document a threat model before any real financial integration
- [ ] Decide whether to merge this with the Floussi Quantum operations prototype

## License

No open-source license is currently declared.
