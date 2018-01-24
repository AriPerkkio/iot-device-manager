USE iotdevicemanager;

INSERT INTO configuration (
    name,
    description,
    json_configuration
)
VALUES (
    "startup configuration",
    "Initial startup configuration for lp-devices",
    "{ \"AUTOSTART_ON_BOOT\": \"TRUE\", \"TXPOWER_LEVEL\": \"LOW\" }"
);

INSERT INTO device_group (
    name,
    description
)
VALUES (
    "LP-DEVICES",
    "Low power devices w/ bgn only"
);

INSERT INTO device_icon (
    path
)
VALUES (
    "lp_devices/80211bng.png"
);

INSERT INTO device_type (
    name,
    device_icon_id
)
VALUES (
    "WiFi bgn",
    (SELECT id FROM device_icon WHERE path="lp_devices/80211bng.png")
);

CALL add_device (
    "LP-Device 1",
    (SELECT id FROM device_type WHERE name="WiFi bgn"),
    (SELECT id FROM device_group WHERE name="LP-DEVICES"),
    (SELECT id FROM configuration WHERE name="startup configuration")
);

INSERT INTO location (
    device_id,
    coordinates,
    time
)
VALUES (
    (SELECT id FROM device where name="LP-Device 1"),
    "52.0800409,5.1273094",
    NOW()
);