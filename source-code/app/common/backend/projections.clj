
(ns app.common.backend.projections
    (:require [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; XXX#0780 (app.common.backend.prototypes)
; A get-document-projection függvény a modulok (adatbázisból dokumentumot olvasó)
; resolver függvényeinek kimenetét szűri meg.

(defn get-document-projection
  ; @param (keyword) namespace
  ;
  ; @return
  ;  {:my-namespace/permissions (integer)
  ;   :my-namespace/added-at (integer)
  ;   :my-namespace/added-by (integer)
  ;   :my-namespace/modified-at (integer)}
  [namespace]
  {(keyword/add-namespace namespace :permissions) 0
   (keyword/add-namespace namespace :added-at)    0
   (keyword/add-namespace namespace :added-by)    0
   (keyword/add-namespace namespace :modified-by) 0})
