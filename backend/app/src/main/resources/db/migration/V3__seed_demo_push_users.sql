insert into users (id, auth_provider_id, username, email, profile_pic_url, created_at, updated_at)
values
    (1001, 'demo-evangelos', 'Evangelos', 'evangelos@poopvibe.local', null, now(), now()),
    (1002, 'demo-alex', 'Alex', 'alex@poopvibe.local', null, now(), now())
on conflict (id) do nothing;

insert into friendships (id, requester_id, addressee_id, status, created_at, updated_at)
values (1001, 1001, 1002, 'ACCEPTED', now(), now())
on conflict (requester_id, addressee_id) do nothing;

alter table users alter column id restart with 2000;
alter table friendships alter column id restart with 2000;
