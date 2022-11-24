
(ns site.xgo.pages.main-page.frontend.sections.types.subs
    (:require [re-frame.api :as r]
              [normalize.api :as normalize]))

;; -----------------------------------------------------------------------------
;; ----- Filters -----

(r/reg-sub
 :filters/type
 :<- [:x.db/get-db]
 :<- [:models.selected/types]
 (fn [[db types] [_]]
   (let [first-type (first types)]
      (get-in db [:filters :type]
        (normalize/clean-text (get-in db [:site :types first-type :name]) "-+")))))
     
;; ----- Filters -----
;; -----------------------------------------------------------------------------

(r/reg-sub 
 :types/selected?
 :<- [:filters/type]
 (fn [filters-type [_ type-name]]
    (= filters-type (normalize/clean-text type-name "-+"))))

(r/reg-sub
 :types/selected
 :<- [:types/all]
 :<- [:filters/type]
 (fn [[types filters-type] [_]]
    (let [result  (->> types
                       (filter (fn [[_ v]]
                                  (= filters-type (normalize/clean-text (:name v) "-+")))))]
      (if (empty? result)
        {}
        (-> result first val)))))  

(r/reg-sub
  :types/all
  (fn [db [_]]
    (get-in db [:site :types] {})))

(r/reg-sub
  :types/by-model
  :<- [:types/all]
  :<- [:models.selected/types]
  (fn [[types model-type-ids] [_]]
    (select-keys types model-type-ids)))