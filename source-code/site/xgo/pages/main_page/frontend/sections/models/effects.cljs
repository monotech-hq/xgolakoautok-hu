
(ns site.xgo.pages.main-page.frontend.sections.models.effects
    (:require [re-frame.api  :as r]
              [normalize.api :as normalize]))

(r/reg-event-fx
  :models/select! 
 (fn [{:keys [db]} [_ model-name]]
   (let [model-name (normalize/clean-text model-name "-+")
         category   (-> db (get-in [:filters :category]) (normalize/clean-text "-+"))]
     {:dispatch-n   [[:x.db/set-item! [:filters :model] model-name]]
      :url/set-url! (str category "/" model-name)})))
