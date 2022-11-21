
(ns app.common.frontend.item-picker.subs
    (:require [re-frame.api :as r]
              [vector.api   :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-filters
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:export-filter-f (function)
  ;   :import-id-f (function)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  (r get-item-filters db :my-picker {...})
  ;
  ; @return (vector)
  [db [_ _ {:keys [export-filter-f import-id-f value-path]}]]
  ; 1. Kiolvassa a Re-Frame adatbázisból a value-path útvonalon található
  ;    vektorban felsorolot hivatkozásokat (pl. [{:item/id "my-item" :item/count 42}])
  ; 2. Kiolvassa a hivatkozásokból az elemek azonosítóit.
  ; 3. Abc sorrendbe rendezi az azonosítókat, ezáltal csak akkor változik meg a
  ;    get-item-filters feliratkozás függvény kimenete, ha cserélődnek az elemek,
  ;    és nem reagál a sorrendjük megváltozására, így elkerülhető, hogy az item-lister
  ;    komponens ami prefilter tulajdonságként kapja meg ennek a függvénynek a kimenetét
  ;    újra letöltse az elemeket ha megváltozna az elemek sorrendje és emiatt
  ;    megváltozna ennek a függvénynek a kimenete.
  ; 4. Szűrő elemekre alakítja vissza az elemeket (pl. {:item/id}), így csak az azonosítójukat
  ;    tartalmazzák az elemek és felhasználhatók az item-lister komponens prefilter funkciójában
  ;    a letöltendő elemek szűrésére.
  (-> (get-in         db value-path)
      (vector/->items import-id-f)
      (vector/abc-items)
      (vector/->items export-filter-f)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-picker/get-item-filters :my-picker {...}]
(r/reg-sub :item-picker/get-item-filters get-item-filters)
