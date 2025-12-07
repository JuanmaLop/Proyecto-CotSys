--
-- PostgreSQL database dump
--

\restrict 0sb5K1uPVCSfmcOIBZ3ZAfqt6RPT1byKSf1Tip8qTb9NzSu3HgzPjQkzP026S8A

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

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

--
-- Name: token_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.token_type_enum AS ENUM (
    'BEARER',
    'REFRESH'
);


ALTER TYPE public.token_type_enum OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cliente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cliente (
    id_cliente bigint NOT NULL,
    nombre character varying(100) NOT NULL,
    nit character varying(11) NOT NULL,
    direccion character varying(80) NOT NULL,
    autorrentenedor boolean NOT NULL,
    municipio character varying(80) NOT NULL,
    tipo_regimen character varying(50) NOT NULL
);


ALTER TABLE public.cliente OWNER TO postgres;

--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.cliente ALTER COLUMN id_cliente ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.cliente_id_cliente_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: componente_kit; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.componente_kit (
    id_componente_kit bigint NOT NULL,
    id_kit_solucion bigint NOT NULL,
    id_producto bigint NOT NULL,
    cantidad integer NOT NULL,
    instrucciones character varying(255) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.componente_kit OWNER TO postgres;

--
-- Name: componente_kit_id_componente_kit_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.componente_kit ALTER COLUMN id_componente_kit ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.componente_kit_id_componente_kit_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: cotizacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cotizacion (
    id_cotizacion bigint NOT NULL,
    estado character varying(40) NOT NULL,
    usuario bigint NOT NULL,
    cliente bigint NOT NULL,
    fecha_creacion date NOT NULL,
    fecha_validez date NOT NULL,
    margen_general numeric(5,2) NOT NULL,
    moneda_cotizacion character varying(20) NOT NULL
);


ALTER TABLE public.cotizacion OWNER TO postgres;

--
-- Name: cotizacion_id_cotizacion_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.cotizacion ALTER COLUMN id_cotizacion ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.cotizacion_id_cotizacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: fase_edt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.fase_edt (
    "id_faseEDT" bigint NOT NULL,
    nombre character varying NOT NULL,
    descripcion character varying(255) NOT NULL,
    orden integer NOT NULL,
    cotizacion bigint NOT NULL
);


ALTER TABLE public.fase_edt OWNER TO postgres;

--
-- Name: faseEDT_id_faseEDT_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.fase_edt ALTER COLUMN "id_faseEDT" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."faseEDT_id_faseEDT_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: impuesto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.impuesto (
    id_impuesto bigint NOT NULL,
    tipo character varying(60) NOT NULL,
    descripcion character varying(255) NOT NULL,
    porcentaje numeric(5,2) NOT NULL,
    estado boolean NOT NULL,
    cotizacion bigint NOT NULL
);


ALTER TABLE public.impuesto OWNER TO postgres;

--
-- Name: impuesto_id_impuesto_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.impuesto ALTER COLUMN id_impuesto ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.impuesto_id_impuesto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: item_cotizacion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_cotizacion (
    id_item_cotizacion bigint NOT NULL,
    id_producto bigint NOT NULL,
    id_cotizacion bigint NOT NULL,
    cantidad double precision NOT NULL,
    descripcion_personalizada character varying(255),
    margen_especifico double precision,
    precio_unitario double precision NOT NULL
);


ALTER TABLE public.item_cotizacion OWNER TO postgres;

--
-- Name: item_cotizacio_id_item_cotizacion_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.item_cotizacion ALTER COLUMN id_item_cotizacion ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.item_cotizacio_id_item_cotizacion_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: jwt_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.jwt_token (
    id_token bigint NOT NULL,
    token character varying(1000) NOT NULL,
    tipo_token character varying(255) DEFAULT 'BEARER'::public.token_type_enum NOT NULL,
    revocado boolean NOT NULL,
    expirado boolean NOT NULL,
    usuario bigint NOT NULL
);


ALTER TABLE public.jwt_token OWNER TO postgres;

--
-- Name: jwtToken_id_token_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.jwt_token ALTER COLUMN id_token ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."jwtToken_id_token_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: kit_solucion; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kit_solucion (
    id_kit_solucion bigint NOT NULL,
    nombre character varying(80) NOT NULL,
    descripcion character varying(255) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.kit_solucion OWNER TO postgres;

--
-- Name: kitSolucion_id_kitSolucion_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.kit_solucion ALTER COLUMN id_kit_solucion ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."kitSolucion_id_kitSolucion_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: precio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.precio (
    id_precio bigint NOT NULL,
    "precioCOP" numeric(10,2) NOT NULL,
    "precioUSD" numeric(10,2) NOT NULL,
    "fechaInicio" date NOT NULL,
    "fechaFin" date NOT NULL,
    vigente boolean NOT NULL,
    producto bigint NOT NULL
);


ALTER TABLE public.precio OWNER TO postgres;

--
-- Name: precio_id_precio_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.precio ALTER COLUMN id_precio ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.precio_id_precio_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: producto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.producto (
    id_producto bigint NOT NULL,
    nombre character varying(60) NOT NULL,
    descripcion character varying(255) NOT NULL,
    categoria character varying(60) NOT NULL,
    unidad_medida character varying(60) NOT NULL,
    costo_base double precision NOT NULL,
    moneda_original character varying(20) NOT NULL,
    tipo character varying(60) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE public.producto OWNER TO postgres;

--
-- Name: producto_id_producto_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.producto ALTER COLUMN id_producto ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.producto_id_producto_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: reporte; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reporte (
    id_reporte bigint NOT NULL,
    tipo character varying(40) NOT NULL,
    "fechaGeneracion" date NOT NULL,
    parametros character varying(250) NOT NULL,
    usuario bigint NOT NULL
);


ALTER TABLE public.reporte OWNER TO postgres;

--
-- Name: reporte_id_reporte_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.reporte ALTER COLUMN id_reporte ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.reporte_id_reporte_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id_usuario bigint NOT NULL,
    nombre character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    rol character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.usuario ALTER COLUMN id_usuario ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.usuario_id_usuario_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cliente (id_cliente, nombre, nit, direccion, autorrentenedor, municipio, tipo_regimen) FROM stdin;
6	Cliente prueba	655437734	Calle 55 #77-69	f	La Ceja	Común
7	Cliente prueba 2	5666456676	Calle 87 #15-66	t	Medellín	Común
8	Ramon Soto Puerta	400534456	Calle 56 #33-44	t	Rionegro	Común
9	María Paula Londoño	100456654	Calle 78 #19-20	f	El Retiro	Común
\.


--
-- Data for Name: componente_kit; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.componente_kit (id_componente_kit, id_kit_solucion, id_producto, cantidad, instrucciones, estado) FROM stdin;
1	1	11	2	Usarlo :)	t
2	1	12	6	Usarlo :)	t
3	1	13	10	Agua	t
4	1	14	1	Contratar	t
5	2	16	1	Poner	t
6	2	15	1	Instalar	t
7	2	17	1	Contratar	t
\.


--
-- Data for Name: cotizacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cotizacion (id_cotizacion, estado, usuario, cliente, fecha_creacion, fecha_validez, margen_general, moneda_cotizacion) FROM stdin;
7	Borrador	67	8	2025-12-07	2025-12-09	10.00	COP
10	Borrador	58	6	2025-12-07	2025-12-09	10.00	COP
\.


--
-- Data for Name: fase_edt; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.fase_edt ("id_faseEDT", nombre, descripcion, orden, cotizacion) FROM stdin;
\.


--
-- Data for Name: impuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.impuesto (id_impuesto, tipo, descripcion, porcentaje, estado, cotizacion) FROM stdin;
\.


--
-- Data for Name: item_cotizacion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.item_cotizacion (id_item_cotizacion, id_producto, id_cotizacion, cantidad, descripcion_personalizada, margen_especifico, precio_unitario) FROM stdin;
5	11	7	5	\N	\N	50000
6	12	7	5	\N	\N	50000
7	11	10	2	\N	\N	250000
8	12	10	6	\N	\N	50000
9	13	10	10	\N	\N	35000
10	14	10	1	\N	\N	131.62
11	15	10	1	\N	\N	100000
\.


--
-- Data for Name: jwt_token; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.jwt_token (id_token, token, tipo_token, revocado, expirado, usuario) FROM stdin;
3	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsImVtYWlsIjoiYWRtaW5wcnVlYmFAaW5zdC5jb20iLCJyb2wiOiJBZG1pbmlzdHJhZG9yIiwic3ViIjoiYWRtaW5wcnVlYmFAaW5zdC5jb20iLCJpYXQiOjE3NjI3Mzc4ODMsImV4cCI6MTc2MjgyNDI4M30.px2NcBcnVaMfTc-dt23rwzpltayoC56AXcq0njv840H-eXBT16EVtFM2jLLv8lRK	BEARER	t	t	12
4	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsImVtYWlsIjoiYWRtaW5wcnVlYmFAaW5zdC5jb20iLCJyb2wiOiJBZG1pbmlzdHJhZG9yIiwic3ViIjoiYWRtaW5wcnVlYmFAaW5zdC5jb20iLCJpYXQiOjE3NjI3MzgyOTQsImV4cCI6MTc2MjgyNDY5NH0.slt4aLRzaC7WS_sgkcFHQje_k_qNyWXJUAMqwxeJie8yaMh4L1PsiVFm0ST9Mn95	BEARER	t	t	12
5	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsImVtYWlsIjoiYWRtaW5wcnVlYmFAaW5zdC5jb20iLCJyb2wiOiJBZG1pbmlzdHJhZG9yIiwic3ViIjoiYWRtaW5wcnVlYmFAaW5zdC5jb20iLCJpYXQiOjE3NjI3MzgzMjEsImV4cCI6MTc2MjgyNDcyMX0.I2LczOQGxggjLphi7jRM6mhpR46gBbeYyG5StFbUzRxAh59lOJQQAQWmMqCjZgKl	BEARER	t	t	12
14	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzYwMzE3MSwiZXhwIjoxNzYzNjg5NTcxfQ.M3Iwqup_fvXZ37P45Js0OkKNpzZ-CPmeoEIlYaLqya8Bi4SSI42nMrzxi3oJ50yW	BEARER	t	t	12
18	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzYwNDg1NSwiZXhwIjoxNzYzNjkxMjU1fQ.6Zr_rWsmX5TqScuZhdU2qSgYhu--X29DqHxATw1P-SAOZPLWHzisQIZzzBESDonr	BEARER	t	t	12
19	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzYwNDk5OSwiZXhwIjoxNzYzNjkxMzk5fQ.cHirVzLfFqaySJPnaiNjtLfAN716LiHAL51RnpYNJsCQRiGpiEp31p8OUeYjl1IC	BEARER	t	t	12
21	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzYwNTU0NywiZXhwIjoxNzYzNjkxOTQ3fQ.0uWU2WUly9S5AbMHsYuQ_3GFtfB3LPIrVpeRCTSxKu4SpndO6650vpH1NmrZ223X	BEARER	t	t	12
22	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzY5MjQ1MSwiZXhwIjoxNzYzNzc4ODUxfQ._zJGwzZfRnuHIKnsAAVSLJfT-VNdJQqQJH_ErUWMBKWrkAzpc1Zn4OOMmK5q9tl7	BEARER	t	t	12
23	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzY5MzA5OCwiZXhwIjoxNzYzNzc5NDk4fQ.Nf37p4EQA17lWYRjtGNJt47xB994XKpV-JEzzXFZ0i8OerBzrNzMf7Ua8o96poDO	BEARER	t	t	12
25	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxNSIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsIiwic3ViIjoiY29tZXJjaWFscHJ1ZWJhQGluc3QuY29tIiwiaWF0IjoxNzYzNjkzNzY3LCJleHAiOjE3NjM3ODAxNjd9.TkOnm0ZvDqLs2iWHqThT9w9apQ1m_Yw8aA8ei5mMfaDuMnSh4mNQzYvcFTYnHYxD	BEARER	f	f	15
24	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2MzY5MzYzNCwiZXhwIjoxNzYzNzgwMDM0fQ.DLhZ91AlylhLaOg-fQOBROye5dH_P68Xh-lR_5oC_Uz78p4zQpzmAgdgtfz6k3S6	BEARER	t	t	12
29	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDEzMTA2OCwiZXhwIjoxNzY0MjE3NDY4fQ.OPyW_n8TPfeqC7W0jQu0LocmAAlgDP0y8Pwq6IA6r42a7I5ZWxJnzLotKu69XzTA	BEARER	t	t	12
30	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDEzMTM1NSwiZXhwIjoxNzY0MjE3NzU1fQ.BwqTqz65MbmWV5Vilx9mnn_3taAdDMcZaGzrZEA7-z7yxMrqjkLjCjuHNoPNXJ9U	BEARER	t	t	12
46	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3NzE5NSwiZXhwIjoxNzY0MzYzNTk1fQ.I17dRVO9PabUsBk61MxL-rw7mJXri63NiSN7vQdUxxDz0n1pkklqRNYH7_Dgt5Of	BEARER	t	t	12
47	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3Nzc4NSwiZXhwIjoxNzY0MzY0MTg1fQ.iUskw6hDbEUDzpnyWHSAaadwfBRldfRohtY2jmXtPhcLBJKjUKI7xn0Q1G-84dB_	BEARER	t	t	12
48	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3Nzg4MywiZXhwIjoxNzY0MzY0MjgzfQ.CYKZ7G7xoaEKt7YLQlDp1SC3NID4HdmEnwm4eRPWHKUdLQkTcq2YZuGYyw-EaU3R	BEARER	t	t	12
49	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3ODk5MiwiZXhwIjoxNzY0MzY1MzkyfQ.e9LoY-G4MvNLAWNfP4XQdSKrcjMfBkbr1-aReuwR1Or8hAksIFhOvMITTAuNLU0Q	BEARER	t	t	12
50	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3OTI3MiwiZXhwIjoxNzY0MzY1NjcyfQ.eDvMXuk82h2x0IUkZqtb5Sbrx5KZOU_gc-WtM_PS-gcxYL2nsDm9LkO5-vreZPtv	BEARER	t	t	12
51	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3OTM1MywiZXhwIjoxNzY0MzY1NzUzfQ.-jlSfryxe6Azuqr89j5NIuUcbG628P-AW2CRtR3ApsGL18HCLaotUkU70ye1DHY7	BEARER	t	t	12
52	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDI3OTY1OSwiZXhwIjoxNzY0MzY2MDU5fQ.ZfWtVDLYh7jXeHQFYYf56XQW68ngovDlcMZzGMWMTtDNORRtia6ES-xYSRDmY_Kc	BEARER	t	t	12
53	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDM0ODM1MCwiZXhwIjoxNzY0NDM0NzUwfQ.Z4PQ3cmR74qsWakvPNGhQGoXpuqUBHKPbJD5uesIErE6D9yC1lOAk6SMDssh-ecr	BEARER	t	t	12
54	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxNiIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsIGRlIHBydWViYSIsInN1YiI6InVzdWFyaW9jb21lcmNpYWwyQGluc3QuY29tIiwiaWF0IjoxNzY0MzUyMDc1LCJleHAiOjE3NjQ0Mzg0NzV9.4JFY9z3VTpG7S7kC8Wpb6SYNmktyKo_2Be6WALcHom9l4khuAFt4VAGKPByukxAs	BEARER	t	t	16
56	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxNiIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsIGRlIHBydWViYSIsInN1YiI6InVzdWFyaW9jb21lcmNpYWwyQGluc3QuY29tIiwiaWF0IjoxNzY0MzUyNzAyLCJleHAiOjE3NjQ0MzkxMDJ9.0sbxf2n6nBM4bjM8l1K5BXaO4zF6yb3yVbDvxRW0NbEQfu86Zt69SxS9c-eJtJ-Z	BEARER	t	t	16
58	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1MyIsIm5vbWJyZSI6IlVzdWFyaW8gQWRtaW5pc3RyYWRvciBkZSBwcnVlYmEiLCJyb2wiOiJBZG1pbmlzdHJhZG9yIiwic3ViIjoiYWRtaW5wcnVlYmEyQGluc3QuY29tIiwiaWF0IjoxNzY0MzU0MjY1LCJleHAiOjE3NjQ0NDA2NjV9.N8ujQ4XeHlPb-0QObrS9EGcQpNp6GgmcUPqDLvEY_lyBT1hqXf31NlbOvlicjHCx	BEARER	f	f	53
57	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDM1MjcyOCwiZXhwIjoxNzY0NDM5MTI4fQ.iZBypM4jC_oyBmpvpKF8TecskKqp0K6VuCpO6P18Tjjt09yNUMqcDjOaf8IwkL_6	BEARER	t	t	12
59	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJVc3VhcmlvQWRtaW4iLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5NDY1MSwiZXhwIjoxNzY0ODgxMDUxfQ.U9aqQFYgt9i33ZOvUsbA-o8a2K5GQeHWFjHBci_tSbUIZb8KMHEYwUS8s3xoq04g	BEARER	t	t	12
60	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5NjgxNiwiZXhwIjoxNzY0ODgzMjE2fQ.1Q7FviN0PJEmnx-vXy1tjMAAVec9ITwhA4y6oLnK3fogwhtbqNVG7Dpd2mKtdfc6	BEARER	t	t	12
61	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5Njg3MCwiZXhwIjoxNzY0ODgzMjcwfQ.symUPndbpUS7zbx0SdQzrSGdJhqtXydHS_dYss4XhsS4YLiFpd-iuCgVcLVT8_az	BEARER	t	t	12
62	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5NzkyMiwiZXhwIjoxNzY0ODg0MzIyfQ.MXhH1ciLcnuYWZm4fPdMplYF4n8r_guf3SSbFxWX78a_6MX9s9onTl7gnYtOoMCH	BEARER	t	t	12
63	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5ODgzNywiZXhwIjoxNzY0ODg1MjM3fQ.QZs_HqMfl7VmY42QzoNEDWsb1skIGnvqaxItYUDESK9OVoIEeDSfPUZ3oYjCOZAJ	BEARER	t	t	12
64	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5OTU3OCwiZXhwIjoxNzY0ODg1OTc4fQ.ug6z51GG-oyy-5Jo8p8xbQrejVKWt7DvomIvsOUPOYhuUCqhDq3282jWGArDLSfr	BEARER	t	t	12
65	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0Nzk5NjQ3LCJleHAiOjE3NjQ4ODYwNDd9.7iR7CrV648rf14NUlYfOMQh7Bf7WdMFWxLiHKsuXiUQLuLldlFaltwU9hwozKwuC	BEARER	t	t	58
66	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDc5OTk4MSwiZXhwIjoxNzY0ODg2MzgxfQ.26LxN0Jya9yygthzo0BrKZLoEgslqQS0qlKkNniJD_vsp9W5SAHppcEIR4RmAAwB	BEARER	t	t	12
68	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDgwMDgwMiwiZXhwIjoxNzY0ODg3MjAyfQ.AyymntabrGX1NQyA4NUvG4b2dwRGmOIICxkKAaBuxEN3ZZnVNv7zhAQGMLqdV0as	BEARER	t	t	12
69	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMiIsIm5vbWJyZSI6IlVzdWFyaW9BZG1pbiIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJhZG1pbnBydWViYUBpbnN0LmNvbSIsImlhdCI6MTc2NDgwMDkzNiwiZXhwIjoxNzY0ODg3MzM2fQ.Z6eirsD0hKf-6JCBHNdEgqexgiRF7ho361LG-H2K8xMBz8n2pb-Afx_mrR-KxbNH	BEARER	f	f	12
70	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OSIsIm5vbWJyZSI6Ikplcm9uaW1vIEJldGFuY3VyIiwicm9sIjoiQWRtaW5pc3RyYWRvciIsInN1YiI6Implcm9uaW1vYkBnbWFpbC5jb20iLCJpYXQiOjE3NjQ4MDA5NjMsImV4cCI6MTc2NDg4NzM2M30.I1faXuyTwUZyzEhgP-kn9t9NfIRCxk8nEjl5vBj0G2a3I0m32hybCMSc3MHWYH5d	BEARER	f	f	59
71	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2MCIsIm5vbWJyZSI6Ikp1YW4gRXN0ZWJhbiBBZ3VkZWxvIiwicm9sIjoiQWRtaW5pc3RyYWRvciIsInN1YiI6Imp1YW5lc3RlYmFuYUBpbnN0LmNvbSIsImlhdCI6MTc2NDgwMTA0NiwiZXhwIjoxNzY0ODg3NDQ2fQ.QVd6sn4JjND0NH5w8E9wNNeJ1y_I_XMk2D1DuMrE3pNR-_u2nCdi3belpdc6c3Ph	BEARER	f	f	60
67	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0ODAwNTAzLCJleHAiOjE3NjQ4ODY5MDN9.9fJpg5t4NmsTyLspHFEX_QtCfzUHsCsb5jG1MOvfmIKKxhSVOygmOT_3G87jbS_Z	BEARER	t	t	58
72	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0ODA5Njg2LCJleHAiOjE3NjQ4OTYwODZ9.5XYrqy7noVoKuHA1U-1o10lH_o5cUiMH0TtKkNXpNCT-ot89Q8Fwny9WuHxzW8s2	BEARER	t	t	58
73	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0ODEwNTI3LCJleHAiOjE3NjQ4OTY5Mjd9.0MEqOXUbbL4KOM9mnEIvJ6SjJ26P0XkgZHjla7w2HbRok7EvT-7QDXemr2hTXRq6	BEARER	t	t	58
75	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0ODExMTcyLCJleHAiOjE3NjQ4OTc1NzJ9.v-uTT8UgPJ1rT8gQJrRmakN5g325_0zJ60u9e-xKG6RiZZDyBx8Ganv-p02XlSKU	BEARER	t	t	58
76	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0ODExMjMzLCJleHAiOjE3NjQ4OTc2MzN9.9c0Rp1f8CahNeRJTj0iN02xNZCP4YojAkxir6CNetb-Qv7QWDzCHv_obEsQY5S4I	BEARER	t	t	58
77	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTU0MDA3LCJleHAiOjE3NjUwNDA0MDd9.B2_WNu3RUdjG1WtFjG1d2KVmAdmJzkeYxJDcbQ5UUWwB05FCWx8pu9NUdKEssjV2	BEARER	t	t	58
93	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NiIsInJvbCI6IlTDqWNuaWNvIiwibm9tYnJlIjoiQXJxIFRlY25pY28iLCJzdWIiOiJ0ZWNuaWNvYXJxQGluc3QuY29tIiwiaWF0IjoxNzY0OTY4ODA3LCJleHAiOjE3NjUwNTUyMDd9.6b0FUqDHU-VC8N1Rj_YJXf0T2wkTo-WQNM1lroxbOOhPq8Qwto3YZsnQWE3p9xDv	BEARER	t	t	66
96	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDUzOTQ3LCJleHAiOjE3NjUxNDAzNDd9.KipeAKpw4SpFUoUxHRwnI8_gFxDrfyu94rXfNSzApZokOrQI3nyQiKwiIz7m3lr1	BEARER	t	t	58
97	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NyIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsICMyIiwic3ViIjoidXN1YXJpb2NvbWVyY2lhbHUyQGluc3QuY29tIiwiaWF0IjoxNzY1MDU5ODg0LCJleHAiOjE3NjUxNDYyODR9.J0SVgTFGnsBmq54266g82uPba3hz-Z7ClziTroET20irzbhK_GST5IxsqMAPTQrY	BEARER	t	t	67
79	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTU0MTk2LCJleHAiOjE3NjUwNDA1OTZ9.sEL4ZKvckW4l1YcuWaST-dx_0eaIgh_P6Oebuo9i4A_JvRmskgpLwyQKtiVB-dZq	BEARER	t	t	58
85	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTY1OTA0LCJleHAiOjE3NjUwNTIzMDR9.uA_GbpYBxBKxqCBGsdVHbp2rQvmRGWcKubitfGxSURJXqgv9g-46uP7CS1voHbiT	BEARER	t	t	58
86	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTY2MTEzLCJleHAiOjE3NjUwNTI1MTN9.qQk8VBy39xcr4nP2dcUidd6wnTCe1YCHDxT36ZjhvbcT98d-AnLxuU7fwUeeU7pi	BEARER	t	t	58
88	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NSIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJIb21icmUgQWRtaW4iLCJzdWIiOiJob21icmVhZG1pbkBpbnN0LmNvbSIsImlhdCI6MTc2NDk2NzAzOSwiZXhwIjoxNzY1MDUzNDM5fQ.00oGpbnCX5Rz_-GSur16982H7qxJAgnVWnul43v12oZCPZs2ouKJ-7dc2WGWwHCK	BEARER	t	t	65
89	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NSIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJIb21icmUgQWRtaW4iLCJzdWIiOiJob21icmVhZG1pbkBpbnN0LmNvbSIsImlhdCI6MTc2NDk2NzA3MiwiZXhwIjoxNzY1MDUzNDcyfQ.UFSfomM2gwaPwV-qltdT3iV8hZBy77TlK8a8aP-wnJEh93IlMrkZAVbZ6lM6LRUy	BEARER	f	f	65
99	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NyIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsICMyIiwic3ViIjoidXN1YXJpb2NvbWVyY2lhbHUyQGluc3QuY29tIiwiaWF0IjoxNzY1MDYyNjUxLCJleHAiOjE3NjUxNDkwNTF9.V6a0EMdmCOv1H0_PS1fRT-c5R-J6rV4LJITsqZ0SbyprWduSFuMljBatJ_XA8qB2	BEARER	t	t	67
87	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTY2MTk3LCJleHAiOjE3NjUwNTI1OTd9._vb4xKz6-ecQCHLgRbJpelc4ImaBX7YNgS49IV82FVgCenCqhgvir--qsPGf6ROt	BEARER	t	t	58
91	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTY3MTM2LCJleHAiOjE3NjUwNTM1MzZ9.UHDlL6bP617_Vdnye-9aKLb6Mb7zVrq_a-_xuz1Inyq7yNEreSb7LDiNaLjxdE1q	BEARER	t	t	58
92	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NiIsInJvbCI6IlTDqWNuaWNvIiwibm9tYnJlIjoiQXJxIFRlY25pY28iLCJzdWIiOiJ0ZWNuaWNvYXJxQGluc3QuY29tIiwiaWF0IjoxNzY0OTY4Nzc3LCJleHAiOjE3NjUwNTUxNzd9.xJg-ATLh7wTOYGLxbkOxiCH0vCYzuXOPEO9Eb7TNTz7Xo79-6Chl_SOG-XsJbp86	BEARER	t	t	66
94	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY0OTY4ODI2LCJleHAiOjE3NjUwNTUyMjZ9.rD1ojU2ITMNRLcgNkwcyl9qaKn6ggkH1AJ0Y2Veyr55flKy9i8X1n_nWrx_MQhrB	BEARER	t	t	58
95	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NyIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsICMyIiwic3ViIjoidXN1YXJpb2NvbWVyY2lhbHUyQGluc3QuY29tIiwiaWF0IjoxNzY0OTY4OTA5LCJleHAiOjE3NjUwNTUzMDl9.Clah2Cd3GCyACnxzaAM1oOSFNTeOHzDxs9m4FpjwZTViQMIruw8D8ds_g0osQx9C	BEARER	t	t	67
101	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIxMSIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6InVzdWFyaW9QcnVlYmEiLCJzdWIiOiJ1c3VhcmlvcHJ1ZWJhQGluc3QuY29tIiwiaWF0IjoxNzY1MDYyOTg4LCJleHAiOjE3NjUxNDkzODh9.WvRAfk-U81-tN4XKPIkSN2qirPCzR9B9eCdpx7qwZgqajDj7vHJxPg7LaOCzj3lU	BEARER	f	f	11
98	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDYyMDI5LCJleHAiOjE3NjUxNDg0Mjl9.aJ-fr1dVlPv16ay7pjp7Ab-R2BbNRl6h0Lj26v23xHNfQuJBNEKN9g1B2UPO-CaK	BEARER	t	t	58
102	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDY1MzQwLCJleHAiOjE3NjUxNTE3NDB9._Sr1vMAcdym5i8z2uIM67AcIeRaXjh_YZjt0zshnvz3UooTu27PinfktL3NTi_VR	BEARER	t	t	58
103	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDY1ODY1LCJleHAiOjE3NjUxNTIyNjV9.2-Y11tbVYawR6rO6jOuVSywbk5uvMFroVSckdRRs6JfwMHg2C5HxiLsE05-fuqxY	BEARER	t	t	58
100	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NyIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsICMyIiwic3ViIjoidXN1YXJpb2NvbWVyY2lhbHUyQGluc3QuY29tIiwiaWF0IjoxNzY1MDYyNjk4LCJleHAiOjE3NjUxNDkwOTh9.BmQpe0uuJeVA-1AcUvY1jYEO0-RwOU5ZR9wnQfJlVyOfNi6xh7kFcQ1FNgk8rnVw	BEARER	t	t	67
105	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NyIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsICMyIiwic3ViIjoidXN1YXJpb2NvbWVyY2lhbHUyQGluc3QuY29tIiwiaWF0IjoxNzY1MDczNjIyLCJleHAiOjE3NjUxNjAwMjJ9.nIWkNAgjeP-i5cgK70RItxlHEmW8LKQ5aDwrZ5JTQtenHNQSld82Va83LJpuBuX6	BEARER	t	t	67
104	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDcwNDg3LCJleHAiOjE3NjUxNTY4ODd9.jTed8mB3ROaNd3FKk-KvVsd8Qqq9-Gj5ygA3PZTcWCOf6CWDnhgZQlI88BexBwjM	BEARER	t	t	58
106	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI2NyIsInJvbCI6IkNvbWVyY2lhbCIsIm5vbWJyZSI6IlVzdWFyaW8gQ29tZXJjaWFsICMyIiwic3ViIjoidXN1YXJpb2NvbWVyY2lhbHUyQGluc3QuY29tIiwiaWF0IjoxNzY1MDc1MzUzLCJleHAiOjE3NjUxNjE3NTN9.BBOatPtdIfVNBlIAJgkq-DVOq48Bqzz_yhsiclqczAoS0SWzehZ8zT8eZrAwhsjt	BEARER	f	f	67
107	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJub21icmUiOiJKdWFuIE1hbnVlbCBPY2FtcG8iLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDc4MjIzLCJleHAiOjE3NjUxNjQ2MjN9.CiZmhwyrKBeiCMh5-P4bWTYOQlHoIXdexL9_uR4LJYzf9BxN6Gi0tlZxQPuaXAxV	BEARER	t	t	58
108	eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI1OCIsIm5vbWJyZSI6Ikp1YW4gTWFudWVsIE9jYW1wbyIsInJvbCI6IkFkbWluaXN0cmFkb3IiLCJzdWIiOiJqdWFubWFudWVsb2NhbXBvQGluc3QuY29tIiwiaWF0IjoxNzY1MDc5NDQ0LCJleHAiOjE3NjUxNjU4NDR9.2cTQxd8QPHBBh1ARieI1SUBNcWtSS8NkLTxy06-orZ8AUxSWbGGlfiuNuFSsMU-u	BEARER	f	f	58
\.


--
-- Data for Name: kit_solucion; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.kit_solucion (id_kit_solucion, nombre, descripcion, estado) FROM stdin;
1	Kit de Construcción	Kit para la construcción	t
2	Kit de Pesera	Kit base de pesera	t
\.


--
-- Data for Name: precio; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.precio (id_precio, "precioCOP", "precioUSD", "fechaInicio", "fechaFin", vigente, producto) FROM stdin;
\.


--
-- Data for Name: producto; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.producto (id_producto, nombre, descripcion, categoria, unidad_medida, costo_base, moneda_original, tipo, estado) FROM stdin;
3	Producto Prueba 1	Producto de super prueba	Contruccion	kg	5000	COP	Producto	t
4	Servicio prueba	Servicio de super prueba	Construccion	No aplica	50000	COP	Servicio	t
5	Producto Prueba 2	Producto para uso exclusivo de pruebas	Administracion	m	70000	COP	Producto	t
6	Producto Prueba 3	Producto para uso exclusivo de pruebas	Administracion	km	1000000	COP	Producto	t
7	Servicio prueba 2	Servicio para uso exclusivo de pruebas	Ventas	No aplica	100000	COP	Servicio	t
11	Taladro	Taladro para construcción	Construcción	Unidad	250000	COP	Producto	t
12	Martillo	Martillo para construcción	Construcción	Unidad	50000	COP	Producto	t
13	Cemento	Cemento para construcción	Construcción	Bulto	35000	COP	Producto	t
14	Mano de obra	Mano de obra para la construcción	Construcción	Hora	131.62	USD	Servicio	t
15	Pecera	Pecera de medidas 85x85	Mueble	Unidad	150000	COP	Producto	t
16	Respiradero para peces	Respiradero original	Componentes	Unidad	50000	COP	Producto	t
17	Instalación	Instalación de pecera	Instalaciones	No aplica	10.53	USD	Servicio	t
\.


--
-- Data for Name: reporte; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reporte (id_reporte, tipo, "fechaGeneracion", parametros, usuario) FROM stdin;
\.


--
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuario (id_usuario, nombre, email, rol, password, estado) FROM stdin;
65	Hombre Admin	hombreadmin@inst.com	Administrador	$2a$10$rPJEdtvQM9fbxG28.vlS4.v4pojdSVNA.4UqHL9thvrNwxP4IzCmG	t
66	Arq Tecnico	tecnicoarq@inst.com	Técnico	$2a$10$HvFRe61lw2tUGkU0Yihk2O42dCT9iJ5HTaGzBNsgyKIPYkS566c6q	t
67	Usuario Comercial #2	usuariocomercialu2@inst.com	Comercial	$2a$10$bcdXW8RhLNZ3HXM.d0wc0ufJGekSosYWtBehmjEL9w/moWtVHghq6	t
15	Usuario Comercial	comercialprueba@inst.com	Comercial	$2a$10$9f6KU4nkQkjc5hcctbaqQuRdmohWx4OZwZWowaX4dhv5lYGvNZD2q	f
11	usuarioPrueba	usuarioprueba@inst.com	Comercial	$2a$10$tcg1jynPhAk2HHVomfrO6umIg9Wz4Zm3HFYuwGRA4IwssmolrJSgy	t
12	UsuarioAdmin	adminprueba@inst.com	Administrador	$2a$10$JPT74cpIK0XKr/9knSpX1uj1WPIC5.zeFJc0RAZT6t7uQ71nfuPP2	t
16	Usuario Comercial de prueba	usuariocomercial2@inst.com	Comercial	$2a$10$95yTFeXlKxA7AVU5X741f.UfT3QyQR2B9ksmZggG0avdWDJa/Ondi	t
53	Usuario Administrador de prueba	adminprueba2@inst.com	Administrador	$2a$10$2y6tqs.pb1W7og2VOcdklOuxb8m1deTGGSuStXDlrRcEBQs1bawK2	t
58	Juan Manuel Ocampo	juanmanuelocampo@inst.com	Administrador	$2a$10$BP/jCD0nz8uVUr./lH/Ul.Li1UWLfHTrNpRYyAfZWJYa2FIYNaK2K	t
60	Juan Esteban Agudelo	juanestebana@inst.com	Administrador	$2a$10$BXpExgRhoJ0syKtmqLg0ZeyGkZJb0gJzHE6eoctP9HHMB5Vo7MFle	t
59	Jeronimo Betancur	jeronimob@inst.com	Administrador	$2a$10$ePL2vJN.Q/GlpBGRGWDHqO483.IOoVih4M4EaWxR2vkKPF7xIjaha	t
\.


--
-- Name: cliente_id_cliente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cliente_id_cliente_seq', 9, true);


--
-- Name: componente_kit_id_componente_kit_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.componente_kit_id_componente_kit_seq', 7, true);


--
-- Name: cotizacion_id_cotizacion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cotizacion_id_cotizacion_seq', 10, true);


--
-- Name: faseEDT_id_faseEDT_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."faseEDT_id_faseEDT_seq"', 1, false);


--
-- Name: impuesto_id_impuesto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.impuesto_id_impuesto_seq', 1, false);


--
-- Name: item_cotizacio_id_item_cotizacion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.item_cotizacio_id_item_cotizacion_seq', 11, true);


--
-- Name: jwtToken_id_token_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."jwtToken_id_token_seq"', 108, true);


--
-- Name: kitSolucion_id_kitSolucion_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."kitSolucion_id_kitSolucion_seq"', 3, true);


--
-- Name: precio_id_precio_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.precio_id_precio_seq', 1, false);


--
-- Name: producto_id_producto_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.producto_id_producto_seq', 17, true);


--
-- Name: reporte_id_reporte_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reporte_id_reporte_seq', 1, false);


--
-- Name: usuario_id_usuario_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuario_id_usuario_seq', 67, true);


--
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);


--
-- Name: componente_kit componente_kit_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.componente_kit
    ADD CONSTRAINT componente_kit_pkey PRIMARY KEY (id_componente_kit);


--
-- Name: cotizacion cotizacion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cotizacion
    ADD CONSTRAINT cotizacion_pkey PRIMARY KEY (id_cotizacion);


--
-- Name: fase_edt faseEDT_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fase_edt
    ADD CONSTRAINT "faseEDT_pkey" PRIMARY KEY ("id_faseEDT");


--
-- Name: impuesto impuesto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.impuesto
    ADD CONSTRAINT impuesto_pkey PRIMARY KEY (id_impuesto);


--
-- Name: item_cotizacion item_cotizacio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_cotizacion
    ADD CONSTRAINT item_cotizacio_pkey PRIMARY KEY (id_item_cotizacion);


--
-- Name: jwt_token jwtToken_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jwt_token
    ADD CONSTRAINT "jwtToken_pkey" PRIMARY KEY (id_token);


--
-- Name: jwt_token jwtToken_token_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jwt_token
    ADD CONSTRAINT "jwtToken_token_key" UNIQUE (token);


--
-- Name: kit_solucion kitSolucion_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kit_solucion
    ADD CONSTRAINT "kitSolucion_pkey" PRIMARY KEY (id_kit_solucion);


--
-- Name: precio precio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.precio
    ADD CONSTRAINT precio_pkey PRIMARY KEY (id_precio);


--
-- Name: producto producto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id_producto);


--
-- Name: reporte reporte_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reporte
    ADD CONSTRAINT reporte_pkey PRIMARY KEY (id_reporte);


--
-- Name: usuario usuario_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);


--
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);


--
-- Name: componente_kit FK_componente_kit_kit_solucion; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.componente_kit
    ADD CONSTRAINT "FK_componente_kit_kit_solucion" FOREIGN KEY (id_kit_solucion) REFERENCES public.kit_solucion(id_kit_solucion) NOT VALID;


--
-- Name: componente_kit FK_componente_kit_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.componente_kit
    ADD CONSTRAINT "FK_componente_kit_producto" FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto) NOT VALID;


--
-- Name: cotizacion FK_cotizacion_cliente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cotizacion
    ADD CONSTRAINT "FK_cotizacion_cliente" FOREIGN KEY (cliente) REFERENCES public.cliente(id_cliente) NOT VALID;


--
-- Name: cotizacion FK_cotizacion_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cotizacion
    ADD CONSTRAINT "FK_cotizacion_usuario" FOREIGN KEY (usuario) REFERENCES public.usuario(id_usuario) NOT VALID;


--
-- Name: fase_edt FK_faseEDT_cotizacion; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.fase_edt
    ADD CONSTRAINT "FK_faseEDT_cotizacion" FOREIGN KEY (cotizacion) REFERENCES public.cotizacion(id_cotizacion) NOT VALID;


--
-- Name: impuesto FK_impuesto_cotizacion; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.impuesto
    ADD CONSTRAINT "FK_impuesto_cotizacion" FOREIGN KEY (cotizacion) REFERENCES public.cotizacion(id_cotizacion) NOT VALID;


--
-- Name: jwt_token FK_jwtToken_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.jwt_token
    ADD CONSTRAINT "FK_jwtToken_usuario" FOREIGN KEY (usuario) REFERENCES public.usuario(id_usuario) NOT VALID;


--
-- Name: precio FK_precio_producto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.precio
    ADD CONSTRAINT "FK_precio_producto" FOREIGN KEY (producto) REFERENCES public.producto(id_producto) NOT VALID;


--
-- Name: reporte FK_reporte_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reporte
    ADD CONSTRAINT "FK_reporte_usuario" FOREIGN KEY (usuario) REFERENCES public.usuario(id_usuario) NOT VALID;


--
-- PostgreSQL database dump complete
--

\unrestrict 0sb5K1uPVCSfmcOIBZ3ZAfqt6RPT1byKSf1Tip8qTb9NzSu3HgzPjQkzP026S8A

