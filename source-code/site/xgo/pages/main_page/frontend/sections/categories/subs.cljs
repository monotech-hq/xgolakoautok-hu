
(ns site.xgo.pages.main-page.frontend.sections.categories.subs
  (:require [re-frame.api :as r]
            [mid-fruits.normalize :as normalize]))

;; -----------------------------------------------------------------------------
;; -----------------------------------------------------------------------------

(r/reg-sub
  :category.this
  (fn [db [_]]
    (-> db
        (get-in [:filters :category] "dynamic"))))

(r/reg-sub
  :category.this/selected?
  (fn [db [_ category-name]]
    (-> db
        (get-in [:filters :category] "dynamic")
        (= category-name))))
