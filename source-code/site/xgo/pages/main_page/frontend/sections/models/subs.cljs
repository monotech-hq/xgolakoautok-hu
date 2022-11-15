
(ns site.xgo.pages.main-page.frontend.sections.models.subs
    (:require [re-frame.api :as r]))

(r/reg-sub 
 :models/all
 (fn [db [_]]
   (get-in db [:site :models])))

(r/reg-sub
 :models/selected
 (fn [db [_]]
   (get-in db [:filters :model])))