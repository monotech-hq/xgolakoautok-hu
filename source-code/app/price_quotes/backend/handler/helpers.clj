
(ns app.price-quotes.backend.handler.helpers
    (:require [app.packages.backend.api                :as packages]
              [app.products.backend.api                :as products]
              [app.services.backend.api                :as services]
              [app.price-quotes.backend.handler.config :as handler.config]
              [candy.api                               :refer [return]]
              [format.api                              :as format]
              [gestures.api                            :as gestures]
              [io.api                                  :as io]
              [mid-fruits.map                          :as map]
              [math.api                                :as math]
              [mixed.api                               :as mixed]
              [mid-fruits.string                       :as string]
              [mongo-db.api                            :as mongo-db]
              [pathom.api                              :as pathom]
              [re-frame.api                            :as r]
              [time.api                                :as time]
              [x.locales.api                           :as x.locales]
              [x.media.api                             :as x.media]
              [x.user.api                              :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-annual-count
  ; @return (integer)
  []
  (let [current-year       (time/get-year)
        price-quote-config (io/read-edn-file handler.config/PRICE-QUOTE-CONFIG-FILEPATH)]
       (get-in price-quote-config [:annual-count current-year] 0)))

(defn increase-annual-count!
  []
  (let [current-year (time/get-year)]
       (letfn [(f [%] (if % (inc %) 1))]
              (io/swap-edn-file! handler.config/PRICE-QUOTE-CONFIG-FILEPATH update-in [:annual-count current-year] f))))
       ; BUG#7180
       ; Így valamiért nem működött, amikor a fájl tartalma még csak egy üres térkép volt!
       ; (io/swap-edn-file! handler.config/PRICE-QUOTE-CONFIG-FILEPATH update-in [:annual-count current-year] inc)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn unparse-dates
  ; @param (namespaced map) price-quote-item
  ;
  ; @return (namespaced map)
  [price-quote-item]
  ; A mongo-db a dokumentumban tárolt "2020-04-20" formátumú dátumokat felismeri
  ; és "2020-04-20T00:00:00.000Z" formátumú időbélyegzővé alakítja.
  ; Szükséges ezeket az időbélyegzőket visszaalakítani!
  (letfn [(f [%] (if (time/timestamp-string?      %)
                     (time/timestamp-string->date %)
                     (return                      %)))]
         (map/->>values price-quote-item f)))

(defn parse-content
  ; @param (string) content
  ;
  ; @example
  ;  (handler.helpers/parse-content "Line #1\nLine #2 @tab My content")
  ;  =>
  ;  [[:span "Line #1"]
  ;   [:span "Line #1" [:span {:style {:padding-left "15px"}}] "My content"]]
  ;
  ; @return (hiccup)
  [content]
  (letfn [(b [result dex x]
             (if (= 0 dex)
                 (conj result                                         (string/trim x))
                 (conj result [:span {:style {:padding-left "15px"}}] (string/trim x))))
          (a [result dex line] (conj result (reduce-kv b [:span] (string/split line #"@tab"))))]
         (reduce-kv a [] (string/split content #"\n"))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn price-quote-name
  ; @param (namespaced map) price-quote-item
  ;
  ; @example
  ;  (handler.helpers/price-quote-name {...})
  ;  =>
  ;  "#2022/0014-3"
  ;
  ; @return (string)
  [{:price-quote/keys [index issue-year version] :as price-quote-item}]
  ; XXX#4077
  ; Az árajánlatokban lehetséges módosítani a verziószámot, ezért mentéskor
  ; szükséges frissíteni a name tulajdonságot, ami tartalmazza az aktuális értékét.
  (let [index (format/leading-zeros index 4)]
       (str "#"issue-year"/"index"-"version)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-vehicle-unit-price
  ; @param (namespaced map) price-quote-item
  ;  {:price-quote/type (namespaced map)
  ;   {:type/id (string)}}}
  ;
  ; @return (integer)
  [{:price-quote/keys [type]}]
  (if-let [{:type/keys [price-margin dealer-rebate manufacturer-price transport-cost]}
           (mongo-db/get-document-by-id "types" (:type/id type))]
          (let [local-price        (+ (or manufacturer-price 0)
                                      (or transport-cost     0))
                local-dealer-price (math/percent-rest local-price        dealer-rebate)
                local-list-price   (math/percent-add  local-dealer-price price-margin)]
               (math/round local-list-price 5))
          (return 0)))

(defn get-more-items-price
  ; @param (namespaced map) price-quote-item
  ;  {:price-quote/packages (namespaced maps in vector)
  ;   :price-quote/products (namespaced maps in vector)
  ;   :price-quote/services (namespaced maps in vector)}
  ;
  ; @return (integer)
  [{:price-quote/keys [packages products services]}]
  (+ (packages/get-packages-price packages)
     (products/get-products-price products)
     (services/get-services-price services)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->cover
  [env]
  (let [price-quote-item (pathom/env->param env :item)
        model-item       (mongo-db/get-document-by-id "models" (-> price-quote-item :price-quote/model :model/id))]
       {:image (-> model-item :model/thumbnail x.media/media-storage-uri->filename x.media/filename->media-storage-filepath)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->informations-content
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->informations-content {...})
  ;  =>
  ;  [[:span "..." [:span {:style {...}}] "..."]
  ;   [:span "..."]]
  ;
  ; @return (hiccups in vector)
  [env]
  (let [price-quote-item  (pathom/env->param env :item)
        template-item     (mongo-db/get-document-by-id "price_quote_templates" (-> price-quote-item :price-quote/template :template/id))
        informations-link (:template/informations template-item)]
       (:content/body (mongo-db/get-document-by-id "contents" (:content/id informations-link)))))

(defn env->informations
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->informations {...})
  ;  =>
  ;  {:content ["..."]}
  ;
  ; @return (map)
  ;  {:content (vector)}
  [env]
  {:content (env->informations-content env)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->footer-content
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->footer-content {...})
  ;  =>
  ;  [[:span "..." [:span {:style {...}}] "..."]
  ;   [:span "..."]]
  ;
  ; @return (hiccups in vector)
  [env]
  (let [price-quote-item (pathom/env->param env :item)
        template-item    (mongo-db/get-document-by-id "price_quote_templates" (-> price-quote-item :price-quote/template :template/id))]
       (-> template-item :template/footer-content parse-content)))

(defn env->footer
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->footer {...})
  ;  =>
  ;  {:content ["..."]}
  ;
  ; @return (map)
  ;  {:content (vector)}
  [env]
  {:content (env->footer-content env)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->body-description
  ; @param (map) env
  ;
  ; @return (string)
  [{:keys [request] :as env}]
  (let [current-price    (x.user/request->user-settings-item request :current-price)
        vat-value        (x.user/request->user-settings-item request :vat-value)
        price-quote-item (pathom/env->param env :item)
        template-item    (mongo-db/get-document-by-id "price_quote_templates" (-> price-quote-item :price-quote/template :template/id))
        body-description (:template/body-description template-item)]
       (gestures/resolve-variable body-description [[current-price                              "@arfolyam"                "@current-price"]
                                                    [vat-value                                  "@afa-ertek"               "@vat-value"]
                                                    [(:template/default-currency template-item) "@alapertelmezett-penznem" "@default-currency"]])))

(defn env->body-subtitle
  ; @param (map) env
  ;
  ; @return (string)
  [env]
  (let [price-quote-item    (pathom/env->param env :item)
        template-item       (mongo-db/get-document-by-id "price_quote_templates" (-> price-quote-item :price-quote/template :template/id))
        model-item          (mongo-db/get-document-by-id "models" (-> price-quote-item :price-quote/model :model/id))
        type-item           (mongo-db/get-document-by-id "types"  (-> price-quote-item :price-quote/type  :type/id))
        product-description (-> model-item :model/product-description string/lowercase)
        body-subtitle       (:template/body-subtitle template-item)]
       (gestures/resolve-variable body-subtitle [[product-description                           "@modell-termek-megnevezese" "@model-product-description"]
                                                 [(:price-quote/vehicle-count price-quote-item) "@jarmu-darabszama"          "@vehicle-count"]
                                                 [(:model/name model-item)                      "@modell-neve"               "@model-name"]
                                                 [(:type/name  type-item)                       "@tipus-neve"                "@type-name"]])))

(defn env->body-title
  ; @param (map) env
  ;
  ; @return (string)
  [env]
  (let [price-quote-item (pathom/env->param env :item)
        template-item    (mongo-db/get-document-by-id "price_quote_templates" (-> price-quote-item :price-quote/template :template/id))
        client-item      (mongo-db/get-document-by-id "clients" (-> price-quote-item :price-quote/client :client/id))
        body-title       (:template/body-title template-item)]
       (gestures/resolve-variable body-title [[(:client/first-name client-item) "@keresztnev" "@first-name"]
                                              [(:client/last-name  client-item) "@vezeteknev" "@last-name"]])))

(defn env->body
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->body {...})
  ;  =>
  ;  {:description "..."
  ;   :subtitle    "..."
  ;   :title       "..."}
  ;
  ; @return (map)
  ;  {:description (string)
  ;   :subtitle (string)
  ;   :title (string)}
  [env]
  {:description (env->body-description env)
   :subtitle    (env->body-subtitle    env)
   :title       (env->body-title       env)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->customer
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->customer {...})
  ;  =>
  ;  {:address "..."
  ;   :name    "My Client"
  ;   :vat-no  "..."}
  ;
  ; @return (map)
  ;  {:address (string)
  ;   :name (string)
  ;   :vat-no (string)}
  [env]
  (let [{:price-quote/keys [client template]} (pathom/env->param env :item)
        {:template/keys    [language]}        (mongo-db/get-document-by-id "price_quote_templates" (:template/id template))
        {:client/keys [address city company-name country first-name last-name zip-code vat-no]}
        (mongo-db/get-document-by-id "clients" (:client/id client))
        client-name    @(r/subscribe [:x.locales/get-ordered-name    first-name last-name language])
        client-address @(r/subscribe [:x.locales/get-ordered-address nil nil city address language])]
       {:address client-address
        :vat-no  vat-no
        :name (or company-name client-name)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->issuer-details
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->issuer-details {...})
  ;  =>
  ;  [[:span "..." [:span {:style {...}}] "..."]
  ;   [:span "..."]]
  ;
  ; @return (hiccups in vector)
  [env]
  (let [price-quote-item (pathom/env->param env :item)
        template-item    (mongo-db/get-document-by-id "price_quote_templates" (-> price-quote-item :price-quote/template :template/id))]
       (-> template-item :template/issuer-details parse-content)))

(defn env->issuer
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->issuer {...})
  ;  =>
  ;  {:details ["My detail #1" "My detail #2"]
  ;   :logo    "environment/media/storage/..."
  ;   :name    "My Issuer"}
  ;
  ; @return (map)
  ;  {:details (strings in vector)
  ;   :logo (string)
  ;   :name (string)}
  [env]
  (let [{:price-quote/keys [client template]}                     (pathom/env->param env :item)
        {:template/keys [issuer-details issuer-logo issuer-name]} (mongo-db/get-document-by-id "price_quote_templates" (:template/id template))]
       {:details (env->issuer-details env)
        :logo    (-> issuer-logo x.media/media-storage-uri->filename x.media/filename->media-storage-filepath)
        :name    issuer-name}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->validity-date
  ; @param (map) env
  ;  {:price-quote/release-date (string)(opt)
  ;   :price-quote/validity-interval (integer or string)(opt)}
  ;
  ; @example
  ;  (handler.helpers/env->validity-date {...})
  ;  =>
  ;  "2020-07-19"
  ;
  ; @return (string)
  [env]
  (let [{:price-quote/keys [release-date validity-interval]} (pathom/env->param  env :item)]
       (if (and release-date validity-interval)
           (-> (time/plus (-> release-date                      time/parse-date)
                          (-> validity-interval mixed/to-number time/days))
               (time/unparse-timestamp)
               (time/timestamp-string->date)))))

(defn env->price-quote
  ; @param (map) env
  ;
  ; @example
  ;  (handler.helpers/env->price-quote {...})
  ;  =>
  ;  {:name          "#2020/0001-1"
  ;   :release-date  "2020-04-20"
  ;   :validity-date "2020-07-19"}
  ;
  ; @return (map)
  ;  {:name (string)
  ;   :release-date (string)
  ;   :validity-date (string)}
  [env]
  (let [{:price-quote/keys [name release-date]} (pathom/env->param  env :item)
        validity-date                           (env->validity-date env)]
       {:name          name
        :release-date  release-date
        :validity-date validity-date}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->items
  [env]
  [{:product-description [:strong "Wörmann Basic L 1325/215 utánfutó nettó listaára"]
    :quantity            "1 db"
    :unit-price          "6487 EUR"
    :total-price         "6487 EUR"}
   {:product-description "Egyedi engedmény magyarországi vásárlók részére"
    :unit-price          "-727 EUR"
    :total-price         "-727 EUR"}
   {:product-description "Megengedett legnagyobb össztömeg csökkentése 1000/1100/1200/1300/1400 kg-ra"
    :unit-price          "50 EUR"
    :total-price         "50 EUR"}
   {:product-description "Rakomány-rögzítő rúd"
    :quantity            "3 db"
    :unit-price          "75 EUR"
    :total-price         "225 EUR"}
   {}
   {:product-description [:strong "Nettó egyedi ár"]
    :total-price         [:strong "6035 EUR"]}
   {:product-description [:strong "27% ÁFA"]
    :total-price         [:strong "1629 EUR"]}
   {:product-description [:strong {:style {:color "#000"}} "Bruttó fizetendő"]
    :total-price         [:strong {:style {:color "#000"}} "7664 EUR"]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn env->template-props
  ; @param (map) env
  ;
  ; @return
  ;  {:body (map)
  ;   :customer (map)
  ;   :issuer (map)
  ;   :price-quote (map)
  ;   :language (keyword)}
  [env]
  (let [{:price-quote/keys [template]} (pathom/env->param env :item)
        {:template/keys    [language]} (mongo-db/get-document-by-id "price_quote_templates" (:template/id template))]
       {:body         (env->body         env)
        :cover        (env->cover        env)
        :customer     (env->customer     env)
        :footer       (env->footer       env)
        :informations (env->informations env)
        :issuer       (env->issuer       env)
        :items        (env->items        env)
        :price-quote  (env->price-quote  env)
        :language     language}))

(defn env->page-props
  ; @param (map) env
  ;  {:request (map)}
  ;
  ; @example
  ;  (handler.helpers/env->page-props {...})
  ;  =>
  ;  {:author  "My Name"
  ;   :subject "Price quote – 2020-04-20"
  ;   :title   "Price quote"}
  ;
  ; @return
  ;  {:author (string)
  ;   :subject (string)
  ;   :title (string)}
  [{:keys [request] :as env}]
  (let [{:price-quote/keys [template]} (pathom/env->param env :item)
        {:template/keys    [language]} (mongo-db/get-document-by-id "price_quote_templates" (:template/id template))
        date   (time/get-date)
        title @(r/subscribe [:x.dictionary/look-up :price-quote {:language language}])]
       {:author  (x.locales/request->ordered-user-name request)
        :subject (str title " – " date)
        :title   title}))
