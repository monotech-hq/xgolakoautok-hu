
(ns site.xgo.pages.main-page.frontend.sections.models.subs
    (:require [re-frame.api :as r]
              [mid-fruits.normalize :as normalize]))

;; -----------------------------------------------------------------------------
;; ----- Filters -----

(r/reg-sub
 :filters/model
 (fn [db [_]]
   (get-in db [:filters :model])))

;; ----- Filters -----
;; -----------------------------------------------------------------------------

;; -----------------------------------------------------------------------------
;; ----- Models -----

(r/reg-sub
  :models/all
  (fn [db [_]]
    (get-in db [:site :models])))

(r/reg-sub
  :models/by-category
  :<- [:models/all]
  :<- [:categories.selected/models]
  (fn [[models category-model-ids] [_]]
    (select-keys models category-model-ids)))

(r/reg-sub
  :models/selected
  :<- [:models/all]
  :<- [:filters/model]
  (fn [[models filters-model] [_]]
    (->> models
        (filter (fn [[_ v]]
                   (= filters-model (normalize/clean-text (:model/name v) "-+"))))
        first 
        val)))

(r/reg-sub
  :models.selected/name
  :<- [:models/selected]
  (fn [model [_]]
    (:model/name model)))

(r/reg-sub
  :models.selected/types
  :<- [:models/selected]
  (fn [{:model/keys [types]} [_]]
    (mapv :type/id types)))


;; ----- Models -----
;; -----------------------------------------------------------------------------