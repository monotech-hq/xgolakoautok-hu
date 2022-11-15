
(ns app.storage.frontend.alias-editor.effects
    (:require [app.storage.frontend.alias-editor.views :as alias-editor.views]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.alias-editor/load-editor!
  (fn [{:keys [db]} [_ media-item]]
      [:x.ui/render-popup! :storage.alias-editor/view
                           {:content [alias-editor.views/view media-item]}]))

(r/reg-event-fx :storage.alias-editor/update-item-alias!
  (fn [{:keys [db]} [_ {:keys [alias id] :as media-item}]]
      (let [updated-alias (get-in db [:storage :alias-editor/item-alias])]
           {:dispatch [:x.ui/remove-popup! :storage.alias-editor/view]
            :dispatch-if [(not= alias updated-alias)
                          [:item-browser/update-item! :storage.media-browser id {:alias updated-alias}]]})))
