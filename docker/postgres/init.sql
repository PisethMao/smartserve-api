--
-- PostgreSQL database dump
--

\restrict eThgHABxIHiFpoVHq74ec7W16MMibcBhKgKlAwW2hVhPakDoQQ3BaASe2NMOxU3

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.4 (Debian 18.4-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.categories DROP CONSTRAINT IF EXISTS fktdst21nbth66utu5rvifn5x4a;
ALTER TABLE IF EXISTS ONLY public.menu_items DROP CONSTRAINT IF EXISTS fkhaqtoboitpl0n541y0sc753my;
ALTER TABLE IF EXISTS ONLY public.menu_item_variants DROP CONSTRAINT IF EXISTS fke00lfbfou352jr07rf47x59ir;
ALTER TABLE IF EXISTS ONLY public.menu_items DROP CONSTRAINT IF EXISTS fk5bg0vbmql5ggu48n7d5pwgjg3;
ALTER TABLE IF EXISTS ONLY public.restaurants DROP CONSTRAINT IF EXISTS restaurants_pkey;
ALTER TABLE IF EXISTS ONLY public.menu_items DROP CONSTRAINT IF EXISTS menu_items_pkey;
ALTER TABLE IF EXISTS ONLY public.menu_item_variants DROP CONSTRAINT IF EXISTS menu_item_variants_pkey;
ALTER TABLE IF EXISTS ONLY public.categories DROP CONSTRAINT IF EXISTS categories_pkey;
DROP TABLE IF EXISTS public.restaurants;
DROP TABLE IF EXISTS public.menu_items;
DROP TABLE IF EXISTS public.menu_item_variants;
DROP TABLE IF EXISTS public.categories;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categories; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.categories (
    display_order integer,
    is_deleted boolean NOT NULL,
    status boolean,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    name_en character varying(100) NOT NULL,
    name_kh character varying(100),
    icon_url character varying(255)
);


--
-- Name: menu_item_variants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.menu_item_variants (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    is_deleted boolean NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    availability_status character varying(30) NOT NULL,
    is_default boolean NOT NULL,
    display_order integer NOT NULL,
    name_en character varying(100) NOT NULL,
    name_kh character varying(100),
    price numeric(10,2) NOT NULL,
    menu_item_id uuid NOT NULL,
    CONSTRAINT menu_item_variants_availability_status_check CHECK (((availability_status)::text = ANY ((ARRAY['AVAILABLE'::character varying, 'UNAVAILABLE'::character varying, 'SOLD_OUT'::character varying])::text[])))
);


--
-- Name: menu_items; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.menu_items (
    is_deleted boolean NOT NULL,
    price numeric(19,2) NOT NULL,
    sold_limit integer,
    status boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    category_id uuid NOT NULL,
    id uuid NOT NULL,
    restaurant_id uuid NOT NULL,
    availability_status character varying(30) NOT NULL,
    name_en character varying(150) NOT NULL,
    name_kh character varying(150),
    image_url character varying(500),
    description_en text,
    description_kh text,
    CONSTRAINT menu_items_availability_status_check CHECK (((availability_status)::text = ANY ((ARRAY['AVAILABLE'::character varying, 'UNAVAILABLE'::character varying, 'SOLD_OUT'::character varying])::text[])))
);


--
-- Name: restaurants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.restaurants (
    is_deleted boolean NOT NULL,
    status boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    id uuid NOT NULL,
    phone character varying(30),
    email character varying(150),
    name_en character varying(150) NOT NULL,
    name_kh character varying(150),
    logo_url character varying(500),
    address character varying(255),
    description_en text,
    description_kh text
);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.categories (display_order, is_deleted, status, created_at, updated_at, id, restaurant_id, name_en, name_kh, icon_url) FROM stdin;
3	f	t	2026-06-22 15:40:07.5477+07	2026-06-22 15:40:07.5477+07	daa05902-305b-439a-a576-b59dce0aa4dd	37bfa65d-1349-4df5-97f1-a2d81558d3bc	Dessert	បង្អែម	https://example.com/icons/dessert.png
1	f	t	2026-06-22 15:49:13.200643+07	2026-06-22 15:49:13.200643+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	37bfa65d-1349-4df5-97f1-a2d81558d3bc	Drink	ភេសជ្ជៈ	https://example.com/icons/dessert.png
2	f	t	2026-06-22 19:48:44.997005+07	2026-06-22 19:48:44.997005+07	1b4df78c-8d9e-4401-af66-b232da3acceb	37bfa65d-1349-4df5-97f1-a2d81558d3bc	Food	ម្ហូប	https://example.com/icons/food.png
\.


--
-- Data for Name: menu_item_variants; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.menu_item_variants (id, created_at, is_deleted, updated_at, availability_status, is_default, display_order, name_en, name_kh, price, menu_item_id) FROM stdin;
b2b49618-a5ea-47b9-afc1-380cdc112e7a	2026-06-23 15:16:42.596211+07	f	2026-06-23 15:37:27.854266+07	UNAVAILABLE	t	1	Large Updated Again	ធំ បានកែប្រែ ម្ដងទៀត	4.25	a1090b38-80ff-4875-8547-0294286a9fa3
9e4b2b13-017a-45a3-824c-6897be44305d	2026-06-23 15:39:14.676559+07	f	2026-06-23 15:39:14.676559+07	AVAILABLE	f	2	Medium	មធ្យម	3.75	a1090b38-80ff-4875-8547-0294286a9fa3
f6eca504-1414-40b3-b3f7-9a749c3f1c9f	2026-06-23 15:38:55.09807+07	t	2026-06-23 15:41:12.646977+07	AVAILABLE	f	1	Small	តូច	3.25	a1090b38-80ff-4875-8547-0294286a9fa3
ef3239b2-5f66-4233-b462-cad767f608d4	2026-06-23 16:25:25.963405+07	f	2026-06-23 16:37:00.607185+07	UNAVAILABLE	f	4	Hot Changed	ក្តៅ បានកែប្រែ	3.50	a1090b38-80ff-4875-8547-0294286a9fa3
486effa3-36fb-4b75-b2b8-9379a8cc032c	2026-06-23 16:04:09.147819+07	t	2026-06-23 16:37:36.020476+07	AVAILABLE	f	3	Extra Large	????????	5.25	a1090b38-80ff-4875-8547-0294286a9fa3
7f31e745-0579-40de-bcc2-5353ae30bea3	2026-06-23 16:44:22.261666+07	t	2026-06-23 16:47:12.793946+07	AVAILABLE	f	0	Hot	ក្តៅ	3.50	a1090b38-80ff-4875-8547-0294286a9fa3
\.


--
-- Data for Name: menu_items; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.menu_items (is_deleted, price, sold_limit, status, created_at, updated_at, category_id, id, restaurant_id, availability_status, name_en, name_kh, image_url, description_en, description_kh) FROM stdin;
f	3.50	50	t	2026-06-22 15:43:03.720763+07	2026-06-22 15:43:03.720763+07	daa05902-305b-439a-a576-b59dce0aa4dd	406222ae-314f-4d14-a5ae-d3a5f80fccb3	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Chocolate Cake	នំសូកូឡា	https://example.com/images/chocolate-cake.png	Soft chocolate cake with cream topping	នំសូកូឡាទន់ មានក្រែមពីលើ
f	3.25	60	t	2026-06-22 16:36:02.332369+07	2026-06-22 16:36:02.332369+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	a1090b38-80ff-4875-8547-0294286a9fa3	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Matcha Latte	ម៉ាចាឡាតេ	https://example.com/images/matcha-latte.png	Japanese matcha latte with fresh milk	ម៉ាចាឡាតេបែបជប៉ុន ជាមួយទឹកដោះគោស្រស់
t	3.00	80	t	2026-06-22 16:14:54.214218+07	2026-06-22 16:39:49.211847+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	83400580-92f2-4d26-8410-1ad45a181ef6	37bfa65d-1349-4df5-97f1-a2d81558d3bc	UNAVAILABLE	Hot Latte	កាហ្វេក្តៅឡាតេ	https://example.com/images/hot-latte.png	Hot latte with fresh milk and espresso	កាហ្វេក្តៅឡាតេ ជាមួយទឹកដោះគោស្រស់ និងអេស្ព្រេសសូ
t	3.25	60	t	2026-06-22 16:41:43.16272+07	2026-06-22 16:43:35.942214+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	49149824-d2ad-4a43-a303-f3d9af875bfe	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Matcha Latte	ម៉ាចាឡាតេ	https://example.com/images/matcha-latte.png	Japanese matcha latte with fresh milk	ម៉ាចាឡាតេបែបជប៉ុន ជាមួយទឹកដោះគោស្រស់
t	3.25	60	t	2026-06-22 16:41:41.893654+07	2026-06-22 16:44:03.51132+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	004b8d29-ea61-4b11-bf0e-2f7d46347da9	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Matcha Latte	ម៉ាចាឡាតេ	https://example.com/images/matcha-latte.png	Japanese matcha latte with fresh milk	ម៉ាចាឡាតេបែបជប៉ុន ជាមួយទឹកដោះគោស្រស់
t	3.25	60	t	2026-06-22 16:41:41.046029+07	2026-06-22 16:44:16.416806+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	76053051-8663-4362-9bda-6339f98d29f5	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Matcha Latte	ម៉ាចាឡាតេ	https://example.com/images/matcha-latte.png	Japanese matcha latte with fresh milk	ម៉ាចាឡាតេបែបជប៉ុន ជាមួយទឹកដោះគោស្រស់
t	3.25	60	t	2026-06-22 16:41:31.164298+07	2026-06-22 16:44:37.754594+07	17a2a2e7-b0c0-475b-9cbb-d74743466d52	0565d939-2d57-428e-8c6b-b23a7be94f90	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Matcha Latte	ម៉ាចាឡាតេ	https://example.com/images/matcha-latte.png	Japanese matcha latte with fresh milk	ម៉ាចាឡាតេបែបជប៉ុន ជាមួយទឹកដោះគោស្រស់
f	12000.00	50	t	2026-06-22 19:49:25.805486+07	2026-06-22 19:49:25.805486+07	1b4df78c-8d9e-4401-af66-b232da3acceb	33a2dd6b-9ec5-4082-a44e-90a72d6fd204	37bfa65d-1349-4df5-97f1-a2d81558d3bc	AVAILABLE	Chicken Fried Rice	បាយឆាមាន់	https://example.com/images/chicken-fried-rice.jpg	Fried rice with chicken, egg, vegetables, and house seasoning.	បាយឆាជាមួយសាច់មាន់ ពងមាន់ បន្លែ និងគ្រឿងទេសពិសេសរបស់ហាង។
t	1.34	0	t	2026-06-22 16:30:43.429128+07	2026-06-22 21:24:58.852293+07	daa05902-305b-439a-a576-b59dce0aa4dd	bc09fa9f-c95e-4793-92c8-6409935834cd	37bfa65d-1349-4df5-97f1-a2d81558d3bc	UNAVAILABLE	string	string	string	string	string
\.


--
-- Data for Name: restaurants; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.restaurants (is_deleted, status, created_at, updated_at, id, phone, email, name_en, name_kh, logo_url, address, description_en, description_kh) FROM stdin;
f	t	2026-06-22 15:39:33.78445+07	2026-06-22 15:39:33.78445+07	37bfa65d-1349-4df5-97f1-a2d81558d3bc	012345678	smartserve@example.com	Ah Seth Restaurant	ភោជនីយដ្ឋាន Ah Seth	https://example.com/logo.png	Phnom Penh, Cambodia	A modern restaurant using QR code ordering.	ភោជនីយដ្ឋានទំនើបប្រើ QR Code សម្រាប់ការកុម្ម៉ង់ម្ហូប។
\.


--
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (id);


--
-- Name: menu_item_variants menu_item_variants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.menu_item_variants
    ADD CONSTRAINT menu_item_variants_pkey PRIMARY KEY (id);


--
-- Name: menu_items menu_items_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.menu_items
    ADD CONSTRAINT menu_items_pkey PRIMARY KEY (id);


--
-- Name: restaurants restaurants_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.restaurants
    ADD CONSTRAINT restaurants_pkey PRIMARY KEY (id);


--
-- Name: menu_items fk5bg0vbmql5ggu48n7d5pwgjg3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.menu_items
    ADD CONSTRAINT fk5bg0vbmql5ggu48n7d5pwgjg3 FOREIGN KEY (category_id) REFERENCES public.categories(id);


--
-- Name: menu_item_variants fke00lfbfou352jr07rf47x59ir; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.menu_item_variants
    ADD CONSTRAINT fke00lfbfou352jr07rf47x59ir FOREIGN KEY (menu_item_id) REFERENCES public.menu_items(id);


--
-- Name: menu_items fkhaqtoboitpl0n541y0sc753my; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.menu_items
    ADD CONSTRAINT fkhaqtoboitpl0n541y0sc753my FOREIGN KEY (restaurant_id) REFERENCES public.restaurants(id);


--
-- Name: categories fktdst21nbth66utu5rvifn5x4a; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT fktdst21nbth66utu5rvifn5x4a FOREIGN KEY (restaurant_id) REFERENCES public.restaurants(id);


--
-- PostgreSQL database dump complete
--

\unrestrict eThgHABxIHiFpoVHq74ec7W16MMibcBhKgKlAwW2hVhPakDoQQ3BaASe2NMOxU3

