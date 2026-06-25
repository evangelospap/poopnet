alter table devices add column endpoint varchar(2048);
alter table devices add column p256dh_key varchar(255) not null default '';
alter table devices add column auth_key varchar(255) not null default '';
alter table devices add column user_agent varchar(500);
alter table devices add column last_push_success_at timestamp with time zone;
alter table devices add column last_push_failure_at timestamp with time zone;
alter table devices add column last_push_failure_reason varchar(500);

update devices set endpoint = fcm_token where endpoint is null;

alter table devices alter column endpoint set not null;
alter table devices drop constraint uk_devices_fcm_token;
alter table devices drop column fcm_token;
alter table devices add constraint uk_devices_endpoint unique (endpoint);

create index idx_devices_user_push_enabled on devices (user_id, push_enabled);
