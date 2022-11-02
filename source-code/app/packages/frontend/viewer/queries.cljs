
(ns app.packages.frontend.viewer.queries
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-package-products-query
  ; @param (maps in vector) package-products
  ;
  ; @usage
  ;  (r get-save-package-products-query db [{...}])
  ;
  ; @return (vector)
  [db [_ package-products]]
  (let [package-id (get-in db [:packages :viewer/viewed-item :id])]
       [`(~(symbol :packages.viewer/save-package-products!)
          ~{:package-id       package-id
            :package-products package-products})]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-save-package-services-query
  ; @param (maps in vector) package-services
  ;
  ; @usage
  ;  (r get-save-package-services-query db [{...}])
  ;
  ; @return (vector)
  [db [_ package-services]]
  (let [package-id (get-in db [:packages :viewer/viewed-item :id])]
       [`(~(symbol :packages.viewer/save-package-services!)
          ~{:package-id       package-id
            :package-services package-services})]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-automatic-price-query
  ; @usage
  ;  (request-automatic-price-query)
  ;
  ; @return (vector)
  []
  (let [package-id @(r/subscribe [:db/get-item [:packages :viewer/viewed-item :id]])]
       [`(~:packages.viewer/get-automatic-price ~{:package-id package-id})]))
