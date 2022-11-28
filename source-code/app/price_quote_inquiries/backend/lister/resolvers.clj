
(ns app.price-quote-inquiries.backend.lister.resolvers
    (:require [app.common.backend.api                :as common]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]
              [engines.item-lister.api               :as item-lister]
              [mongo-db.api                          :as mongo-db]
              [pathom.api                            :as pathom]
              [x.user.api                            :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-items-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:document-count (integer)
  ;   :documents (namespaced maps in vector)}
  [{:keys [request] :as env} _]
  (if (x.user/request->authenticated? request)
      (let [get-pipeline   (item-lister/env->get-pipeline   env :price-quote-inquiries.lister)
            count-pipeline (item-lister/env->count-pipeline env :price-quote-inquiries.lister)]
           {:items          (mongo-db/get-documents-by-pipeline   "price_quote_inquiries" get-pipeline)
            :all-item-count (mongo-db/count-documents-by-pipeline "price_quote_inquiries" count-pipeline)})))

(defresolver get-items
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
             ;  {:price-quote-inquiries.lister/get-items (map)
             ;    {:document-count (integer)
             ;     :documents (namespaced maps in vector)}}
             [env resolver-props]
             {:price-quote-inquiries.lister/get-items (get-items-f env resolver-props)})

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-items])

(pathom/reg-handlers! ::handlers HANDLERS)
