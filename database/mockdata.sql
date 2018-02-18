CALL add_configuration(
    "startup configuration",
    "Initial startup configuration for lp-devices",
    '{"AUTOSTART_ON_BOOT": "TRUE", "TXPOWER_LEVEL": "LOW"}'
);

CALL add_device_group(
    "LP-DEVICES",
    "Low power devices w/ bgn only"
);

CALL add_device_icon(
    "icons/bticon.png"
);

CALL add_device_type(
    "WiFi bgn",
    1
);

CALL add_device (
    "LP-Device 1",
    1,
    1,
    1
);

CALL add_location(
    1,
    52.0800409,
    5.1273094,
    NULL
);