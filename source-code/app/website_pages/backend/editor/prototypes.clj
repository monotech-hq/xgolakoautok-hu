
(ns app.website-pages.backend.editor.prototypes
    (:require [app.common.backend.api :as common]
              [candy.api              :refer [return]]
              [re-frame.api           :as r]
              [string.api             :as string]
              [uri.api                :as uri]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn added-document-prototype
  ; @param (namespaced map) document
  ;  {:page/automatic-link? (boolean)(opt)
  ;   :page/name (string)
  ;   :page/public-link (string)(opt)}
  ;
  ; @return (namespaced map)
  ;  {:page/public-link (string)}
  [{:page/keys [automatic-link? name public-link] :as document}]
  (if automatic-link? (update document :page/public-link (fn [_] (common/valid-link name)))
                      (update document :page/public-link (fn [%] (common/valid-link %)))))

(defn updated-document-prototype
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [document]
  (added-document-prototype document))
