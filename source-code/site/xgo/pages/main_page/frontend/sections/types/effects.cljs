
(ns site.xgo.pages.main-page.frontend.sections.types.effects
    (:require [re-frame.api :as r]))

(r/reg-event-fx
 :type/select!
 (fn [_ [_ id]]
  {:dispatch [:x.db/set-item! [:filters :type] id]}))