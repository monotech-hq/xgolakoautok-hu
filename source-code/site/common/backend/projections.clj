
(ns site.common.backend.projections
    (:require [keyword.api :as keyword]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-projection
  ; @param (keyword) namespace
  ;
  ; @return
  ;  {:my-namespace/permissions (integer)
  ;   :my-namespace/added-at (integer)
  ;   :my-namespace/added-by (integer)
  ;   :my-namespace/modified-at (integer)
  ;   :my-namespace/modified-by (integer)}
  [namespace]
  {(keyword/add-namespace namespace :permissions) 0
   (keyword/add-namespace namespace :added-at)    0
   (keyword/add-namespace namespace :added-by)    0
   (keyword/add-namespace namespace :modified-at) 0
   (keyword/add-namespace namespace :modified-by) 0})
