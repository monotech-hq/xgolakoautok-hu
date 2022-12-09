
(ns site.xgo.pages.price-quote.frontend.events
  (:require [re-frame.api :as r]))

(r/reg-event-db 
 :price-quote.item.quantity/inc!
 (fn [db [_ [tab-id item-id]]]
    (update-in db [:price-quote tab-id item-id] inc)))

(r/reg-event-db
 :price-quote.item.quantity/dec!
 (fn [db [_ [tab-id item-id]]]
   (update-in db [:price-quote tab-id item-id] dec)))

(r/reg-event-db
 :price-quote.item/remove!
 (fn [db [_ [tab-id item-id]]]
   (update-in db [:price-quote tab-id] dissoc item-id)))