
(ns site.xgo.pages.main-page.frontend.sections.types.subs
    (:require [re-frame.api :as r]))

;; -----------------------------------------------------------------------------
;; ----- Filters -----

(r/reg-sub
 :filters/type
 :<- [:x.db/get-db]
 :<- [:models.selected/types]
 (fn [[db types] [_]]
   (let [first-type (first types)]
      (get-in db [:filters :type] first-type))))
     
;; ----- Filters -----
;; -----------------------------------------------------------------------------

(r/reg-sub 
 :types/selected?
 :<- [:filters/type]
 (fn [filters-type [_ id]]
    (= filters-type id)))

(r/reg-sub
 :types/selected
 :<- [:types/all]
 :<- [:filters/type]
 (fn [[types filters-type] [_]]
    (get types filters-type)))

(r/reg-sub
  :types/all
  (fn [db [_]]
    (get-in db [:site :types])))

(r/reg-sub
  :types/by-model
  :<- [:types/all]
  :<- [:models.selected/types]
  (fn [[types model-type-ids] [_]]
    (select-keys types model-type-ids)))