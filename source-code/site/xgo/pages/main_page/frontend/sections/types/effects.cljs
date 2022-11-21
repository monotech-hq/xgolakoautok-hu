
(ns site.xgo.pages.main-page.frontend.sections.types.effects
    (:require [re-frame.api :as r]))

(r/reg-event-fx
 :types/select!
 (fn [_ [_ id]]
  {:dispatch [:x.db/set-item! [:filters :type] id]}))

(r/reg-event-fx
 :types.view/back!
 (fn [{:keys []
       {:keys [filters]} :db} [_]]
   (let [category (select-keys filters [:category])]
     {:dispatch           [:x.db/set-item! [:filters] category]
      :url/set-url!       (str "/" (:category category))
      :scroll/scroll-into ["xgo-categories" {:behavior "smooth"
                                             :block    "start"
                                             :inline   "center"}]})))