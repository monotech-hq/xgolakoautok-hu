
(ns app.price-quote-templates.backend.editor.prototypes
    (:require [app.price-quote-templates.mid.handler.config :as handler.config]
              [mid-fruits.candy                             :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-price-quote-template-item-prototype
  ; @param (namespaced map) price-quote-template-item
  ;
  ; @return (namespaced map)
  ;  {:template/default-currency (string)
  ;   :template/language (keyword)}
  [price-quote-template-item]
  ; XXX#5050 (source-code/app/price-quote-templates/frontend/README.md)
  (merge {:template/default-currency handler.config/DEFAULT-DEFAULT-CURRENCY
          :template/language         handler.config/DEFAULT-LANGUAGE}
         (param price-quote-template-item)))

(defn updated-price-quote-template-item-prototype
  ; @param (namespaced map) price-quote-template-item
  ;
  ; @return (namespaced map)
  ;  {:template/default-currency (string)
  ;   :template/language (keyword)}
  [price-quote-template-item]
  ; XXX#5050 (source-code/app/price-quote-templates/frontend/README.md)
  (merge {:template/default-currency handler.config/DEFAULT-DEFAULT-CURRENCY
          :template/language         handler.config/DEFAULT-LANGUAGE}
         (param price-quote-template-item)))
