
(ns app.vehicle-types.frontend.editor.effects
  (:require [app.vehicle-types.frontend.editor.views :as editor.views]
            [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-types.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :vehicle-types.editor
                                                {:default-view-id :data}]
                [:vehicle-types.editor/render-editor!]]})

(r/reg-event-fx :vehicle-types.editor/render-editor!
  [:x.ui/render-surface! :vehicle-types.editor/view
                         {:content #'editor.views/view}])
