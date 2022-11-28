
(ns app.price-quotes.backend.editor.prototypes
    (:require [app.price-quotes.backend.handler.helpers :as handler.helpers]
              [app.price-quotes.mid.handler.config      :as handler.config]
              [candy.api                                :refer [param]]
              [time.api                                 :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  ; Az árajánlathoz a létrehozásakor szükséges hozzáadni ...
  ; ... az aktuális évben hanyadik árajánlat (index)
  ; ... a kiadás éve (issue-year)
  ; ... az árajánlat első verziószáma (version)
  ;
  ; XXX#4077 (source-code/app/price_quotes/backend/handler/helpers.clj)
  ; XXX#5050 (source-code/app/price_quote_templates/frontend/README.md)
  (let [annual-count (handler.helpers/get-annual-count)]
       (as-> {} % (merge % {:price-quote/validity-interval handler.config/DEFAULT-VALIDITY-INTERVAL})
                  (merge % document)
                  (merge % {:price-quote/index      (inc annual-count)
                            :price-quote/issue-year (time/get-year)
                            :price-quote/version    1})
                  ; A price-quote-name függvénynek szükséges van az as-> függvény előző iterációjában
                  ; beállított értékekre, amik új elem létrehozásakor még üresek, ezért az előző iterációban
                  ; kitöltött, % paraméterként átadott elemet kapja meg paraméterként!
                  (merge % {:price-quote/name       (handler.helpers/price-quote-name %)}))))

(defn updated-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  ; XXX#4077 (source-code/app/price_quotes/backend/handler/helpers.clj)
  (merge {:price-quote/validity-interval handler.config/DEFAULT-VALIDITY-INTERVAL}
         (param document)
         {:price-quote/name (handler.helpers/price-quote-name document)}))
