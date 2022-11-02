
(ns app.storage.frontend.directory-creator.validators
    (:require [mid-fruits.map :as map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-directory-response-valid?
  ; @param (keyword) browser-id
  ; @param (map) server-response
  ;
  ; @return (boolean)
  [db [_ creator-id server-response]]
  (let [document (get server-response (symbol "storage.directory-creator/create-directory!"))]
       (map/namespaced? document)))
