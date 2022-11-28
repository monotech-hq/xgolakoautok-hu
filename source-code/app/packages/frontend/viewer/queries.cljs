
(ns app.packages.frontend.viewer.queries
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-automatic-price-query
  ; @usage
  ;  (request-automatic-price-query)
  ;
  ; @return (vector)
  []
  (let [package-id @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :id]])]
       [`(~:packages.viewer/get-automatic-price ~{:package-id package-id})]))
