
(ns site.xgo.pages.price-quote.frontend.subs
   (:require [re-frame.api :as r]))

(r/reg-sub 
 :step.one/valid?
 (fn [db [_]]
   (let [{:keys [name email]} (get db :price-quote)]
     (and (not-empty name) 
          (not-empty email)))))

(r/reg-sub 
 :price-quote/breadcrumbs
 :<- [:categories/selected]
 :<- [:models/selected]
 :<- [:types/selected]
 (fn [[category model type] [_]]
    {:category-name (:name category) 
     :model-name    (:name model)
     :type-name     (:name type)}))

(r/reg-sub 
 :price-quote.item.quantity/get
 (fn [db [_ [tab-id item-id]]]
   (get-in db [:price-quote tab-id item-id] 0)))

(r/reg-sub 
 :price-quote.overview.accessories/get
 (fn [db [_ [accessory-id items]]]
    (-> db 
        (get-in [:site accessory-id])
        (select-keys (keys items)))))