
(ns app.packages.frontend.editor.effects
  (:require [app.packages.frontend.editor.views :as editor.views]
            [re-frame.api                             :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :packages.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :packages.editor
                                                {:default-view-id :data}]
                [:packages.editor/render-editor!]]})

(r/reg-event-fx :packages.editor/render-editor!
  [:x.ui/render-surface! :packages.editor/view
                         {:content #'editor.views/view}])
