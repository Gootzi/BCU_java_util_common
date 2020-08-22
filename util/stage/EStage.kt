package common.util.stage

import common.util.stage.Limit
import common.util.stage.SCDef
import common.util.stage.Stage

import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.DriveScopes
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.client.http.HttpTransport
import com.google.api.services.drive.Drive
import kotlin.Throws
import java.io.IOException
import io.drive.DriveUtil
import java.io.FileNotFoundException
import java.io.FileInputStream
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import java.io.InputStreamReader
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import kotlin.jvm.JvmStatic
import io.drive.DrvieInit
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.model.FileList
import java.io.BufferedInputStream
import java.io.FileOutputStream
import com.google.api.client.googleapis.media.MediaHttpDownloader
import io.WebFileIO
import io.BCJSON
import page.LoadPage
import org.json.JSONObject
import org.json.JSONArray
import main.MainBCU
import main.Opts
import common.CommonStatic
import java.util.TreeMap
import java.util.Arrays
import java.io.BufferedReader
import io.BCMusic
import common.util.stage.Music
import io.BCPlayer
import java.util.HashMap
import javax.sound.sampled.Clip
import java.io.ByteArrayInputStream
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineEvent
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener
import com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadState
import common.io.DataIO
import io.BCUReader
import common.io.InStream
import com.google.gson.JsonElement
import common.io.json.JsonDecoder
import com.google.gson.JsonObject
import page.MainFrame
import page.view.ViewBox.Conf
import page.MainLocale
import page.battle.BattleInfoPage
import page.support.Exporter
import page.support.Importer
import common.pack.Context.ErrType
import common.util.stage.MapColc
import common.util.stage.MapColc.DefMapColc
import common.util.lang.MultiLangCont
import common.util.stage.StageMap
import common.util.unit.Enemy
import io.BCUWriter
import java.text.SimpleDateFormat
import java.io.PrintStream
import common.io.OutStream
import common.battle.BasisSet
import res.AnimatedGifEncoder
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import common.io.json.JsonEncoder
import java.io.FileWriter
import com.google.api.client.http.GenericUrl
import org.apache.http.impl .client.CloseableHttpClient
import org.apache.http.impl .client.HttpClients
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.HttpEntity
import org.apache.http.util.EntityUtils
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandler
import com.google.api.client.util.ExponentialBackOff
import com.google.api.client.http.HttpBackOffIOExceptionHandler
import res.NeuQuant
import res.LZWEncoder
import java.io.BufferedOutputStream
import java.awt.Graphics2D
import java.awt.image.DataBufferByte
import common.system.fake.FakeImage
import utilpc.awt.FIBI
import jogl.util.AmbImage
import common.system.files.VFile
import jogl.util.GLImage
import com.jogamp.opengl.util.texture.TextureData
import common.system.P
import com.jogamp.opengl.util.texture.TextureIO
import jogl.GLStatic
import com.jogamp.opengl.util.texture.awt.AWTTextureIO
import java.awt.AlphaComposite
import common.system.fake.FakeImage.Marker
import jogl.util.GLGraphics
import com.jogamp.opengl.GL2
import jogl.util.GeoAuto
import com.jogamp.opengl.GL2ES3
import com.jogamp.opengl.GL
import common.system.fake.FakeGraphics
import common.system.fake.FakeTransform
import jogl.util.ResManager
import jogl.util.GLGraphics.GeomG
import jogl.util.GLGraphics.GLC
import jogl.util.GLGraphics.GLT
import com.jogamp.opengl.GL2ES2
import com.jogamp.opengl.util.glsl.ShaderCode
import com.jogamp.opengl.util.glsl.ShaderProgram
import com.jogamp.opengl.GLException
import jogl.StdGLC
import jogl.Temp
import common.util.anim.AnimU
import common.util.anim.EAnimU
import jogl.util.GLIB
import javax.swing.JFrame
import common.util.anim.AnimCE
import common.util.anim.AnimU.UType
import com.jogamp.opengl.util.FPSAnimator
import com.jogamp.opengl.GLEventListener
import com.jogamp.opengl.GLAutoDrawable
import page.awt.BBBuilder
import page.battle.BattleBox.OuterBox
import common.battle.SBCtrl
import page.battle.BattleBox
import jogl.GLBattleBox
import common.battle.BattleField
import page.anim.IconBox
import jogl.GLIconBox
import jogl.GLBBRecd
import page.awt.RecdThread
import page.view.ViewBox
import jogl.GLViewBox
import page.view.ViewBox.Controller
import java.awt.AWTException
import page.battle.BBRecd
import jogl.GLRecorder
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.GLCapabilities
import page.anim.IconBox.IBCtrl
import page.anim.IconBox.IBConf
import page.view.ViewBox.VBExporter
import jogl.GLRecdBImg
import page.JTG
import jogl.GLCstd
import jogl.GLVBExporter
import common.util.anim.EAnimI
import page.RetFunc
import page.battle.BattleBox.BBPainter
import page.battle.BBCtrl
import javax.swing.JOptionPane
import kotlin.jvm.Strictfp
import main.Inv
import javax.swing.SwingUtilities
import java.lang.InterruptedException
import utilpc.UtilPC.PCItr
import utilpc.awt.PCIB
import jogl.GLBBB
import page.awt.AWTBBB
import utilpc.Theme
import page.MainPage
import common.io.assets.AssetLoader
import common.pack.Source.Workspace
import common.io.PackLoader.ZipDesc.FileDesc
import common.io.assets.Admin
import page.awt.BattleBoxDef
import page.awt.IconBoxDef
import page.awt.BBRecdAWT
import page.awt.ViewBoxDef
import org.jcodec.api.awt.AWTSequenceEncoder
import page.awt.RecdThread.PNGThread
import page.awt.RecdThread.MP4Thread
import page.awt.RecdThread.GIFThread
import java.awt.GradientPaint
import utilpc.awt.FG2D
import page.anim.TreeCont
import javax.swing.JTree
import javax.swing.event.TreeExpansionListener
import common.util.anim.MaModel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.event.TreeExpansionEvent
import java.util.function.IntPredicate
import javax.swing.tree.DefaultTreeModel
import common.util.anim.EAnimD
import page.anim.AnimBox
import utilpc.PP
import common.CommonStatic.BCAuxAssets
import common.CommonStatic.EditLink
import page.JBTN
import page.anim.DIYViewPage
import page.anim.ImgCutEditPage
import page.anim.MaModelEditPage
import page.anim.MaAnimEditPage
import page.anim.EditHead
import java.awt.event.ActionListener
import page.anim.AbEditPage
import common.util.anim.EAnimS
import page.anim.ModelBox
import common.util.anim.ImgCut
import page.view.AbViewPage
import javax.swing.JList
import javax.swing.JScrollPane
import javax.swing.JComboBox
import utilpc.UtilPC
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import common.system.VImg
import page.support.AnimLCR
import page.support.AnimTable
import common.util.anim.MaAnim
import java.util.EventObject
import javax.swing.text.JTextComponent
import page.anim.PartEditTable
import javax.swing.ListSelectionModel
import page.support.AnimTableTH
import page.JTF
import utilpc.ReColor
import page.anim.ImgCutEditTable
import page.anim.SpriteBox
import page.anim.SpriteEditPage
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import common.pack.PackData.UserPack
import utilpc.Algorithm.SRResult
import page.anim.MaAnimEditTable
import javax.swing.JSlider
import java.awt.event.MouseWheelEvent
import common.util.anim.EPart
import javax.swing.event.ChangeEvent
import page.anim.AdvAnimEditPage
import javax.swing.BorderFactory
import page.JL
import javax.swing.ImageIcon
import page.anim.MMTree
import javax.swing.event.TreeSelectionListener
import javax.swing.event.TreeSelectionEvent
import page.support.AbJTable
import page.anim.MaModelEditTable
import page.info.edit.ProcTable
import page.info.edit.ProcTable.AtkProcTable
import page.info.edit.SwingEditor
import page.info.edit.ProcTable.MainProcTable
import page.support.ListJtfPolicy
import page.info.edit.SwingEditor.SwingEG
import common.util.Data.Proc
import java.lang.Runnable
import javax.swing.JComponent
import page.info.edit.LimitTable
import page.pack.CharaGroupPage
import page.pack.LvRestrictPage
import javax.swing.SwingConstants
import common.util.lang.Editors.EditorGroup
import common.util.lang.Editors.EdiField
import common.util.lang.Editors
import common.util.lang.ProcLang
import page.info.edit.EntityEditPage
import common.util.lang.Editors.EditorSupplier
import common.util.lang.Editors.EditControl
import page.info.edit.SwingEditor.IntEditor
import page.info.edit.SwingEditor.BoolEditor
import page.info.edit.SwingEditor.IdEditor
import page.SupPage
import common.util.unit.AbEnemy
import common.pack.IndexContainer.Indexable
import common.pack.Context.SupExc
import common.battle.data .AtkDataModel
import utilpc.Interpret
import common.battle.data .CustomEntity
import page.info.filter.UnitEditBox
import common.battle.data .CustomUnit
import common.util.stage.SCGroup
import page.info.edit.SCGroupEditTable
import common.util.stage.SCDef
import page.info.filter.EnemyEditBox
import common.battle.data .CustomEnemy
import page.info.StageFilterPage
import page.view.BGViewPage
import page.view.CastleViewPage
import page.view.MusicPage
import common.util.stage.CastleImg
import common.util.stage.CastleList
import java.text.DecimalFormat
import common.util.stage.Recd
import common.util.stage.MapColc.PackMapColc
import page.info.edit.StageEditTable
import page.support.ReorderList
import page.info.edit.HeadEditTable
import page.info.filter.EnemyFindPage
import page.battle.BattleSetupPage
import page.info.edit.AdvStEditPage
import page.battle.StRecdPage
import page.info.edit.LimitEditPage
import page.support.ReorderListener
import common.util.pack.Soul
import page.info.edit.AtkEditTable
import page.info.filter.UnitFindPage
import common.battle.Basis
import common.util.Data.Proc.IMU
import javax.swing.DefaultComboBoxModel
import common.util.Animable
import common.util.pack.Soul.SoulType
import page.view.UnitViewPage
import page.view.EnemyViewPage
import page.info.edit.SwingEditor.EditCtrl
import page.support.Reorderable
import page.info.EnemyInfoPage
import common.util.unit.EneRand
import page.pack.EREditPage
import page.support.InTableTH
import page.support.EnemyTCR
import javax.swing.DefaultListCellRenderer
import page.info.filter.UnitListTable
import page.info.filter.UnitFilterBox
import page.info.filter.EnemyListTable
import page.info.filter.EnemyFilterBox
import page.info.filter.UFBButton
import page.info.filter.UFBList
import common.battle.data .MaskUnit
import javax.swing.AbstractButton
import page.support.SortTable
import page.info.UnitInfoPage
import page.support.UnitTCR
import page.info.filter.EFBButton
import page.info.filter.EFBList
import common.util.stage.LvRestrict
import common.util.stage.CharaGroup
import page.info.StageTable
import page.info.TreaTable
import javax.swing.JPanel
import page.info.UnitInfoTable
import page.basis.BasisPage
import kotlin.jvm.JvmOverloads
import page.info.EnemyInfoTable
import common.util.stage.RandStage
import page.info.StagePage
import page.info.StageRandPage
import common.util.unit.EForm
import page.pack.EREditTable
import common.util.EREnt
import common.pack.FixIndexList
import page.support.UnitLCR
import page.pack.RecdPackPage
import page.pack.CastleEditPage
import page.pack.BGEditPage
import page.pack.CGLREditPage
import common.pack.Source.ZipSource
import page.info.edit.EnemyEditPage
import page.info.edit.StageEditPage
import page.info.StageViewPage
import page.pack.UnitManagePage
import page.pack.MusicEditPage
import page.battle.AbRecdPage
import common.system.files.VFileRoot
import java.awt.Desktop
import common.pack.PackData
import common.util.unit.UnitLevel
import page.info.edit.FormEditPage
import common.util.anim.AnimI
import common.util.anim.AnimI.AnimType
import common.util.anim.AnimD
import common.battle.data .Orb
import page.basis.LineUpBox
import page.basis.LubCont
import common.battle.BasisLU
import page.basis.ComboListTable
import page.basis.ComboList
import page.basis.NyCasBox
import page.basis.UnitFLUPage
import common.util.unit.Combo
import page.basis.LevelEditPage
import common.util.pack.NyCastle
import common.battle.LineUp
import common.system.SymCoord
import java.util.TreeSet
import page.basis.OrbBox
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.JTable
import common.CommonStatic.BattleConst
import common.battle.StageBasis
import common.util.ImgCore
import common.battle.attack.ContAb
import common.battle.entity.EAnimCont
import common.battle.entity.WaprCont
import page.battle.RecdManagePage
import page.battle.ComingTable
import common.util.stage.EStage
import page.battle.EntityTable
import common.battle.data .MaskEnemy
import common.battle.SBRply
import common.battle.entity.AbEntity
import page.battle.RecdSavePage
import page.LocComp
import page.LocSubComp
import javax.swing.table.TableModel
import page.support.TModel
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableColumnModel
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.TransferHandler
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import javax.swing.DropMode
import javax.swing.TransferHandler.TransferSupport
import java.awt.dnd.DragSource
import java.awt.datatransfer.UnsupportedFlavorException
import common.system.Copable
import page.support.AnimTransfer
import javax.swing.DefaultListModel
import page.support.InListTH
import java.awt.FocusTraversalPolicy
import javax.swing.JTextField
import page.CustomComp
import javax.swing.JToggleButton
import javax.swing.JButton
import javax.swing.ToolTipManager
import javax.swing.JRootPane
import javax.swing.JProgressBar
import page.ConfigPage
import page.view.EffectViewPage
import page.pack.PackEditPage
import page.pack.ResourcePage
import javax.swing.WindowConstants
import java.awt.event.AWTEventListener
import java.awt.AWTEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.ConcurrentModificationException
import javax.swing.plaf.FontUIResource
import java.util.Enumeration
import javax.swing.UIManager
import common.CommonStatic.FakeKey
import page.LocSubComp.LocBinder
import page.LSCPop
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JTextPane
import page.TTT
import java.util.ResourceBundle
import java.util.MissingResourceException
import java.util.Locale
import common.io.json.Test.JsonTest_2
import common.pack.PackData.PackDesc
import common.io.PackLoader
import common.io.PackLoader.Preload
import common.io.PackLoader.ZipDesc
import common.io.json.Test
import common.io.json.JsonClass
import common.io.json.JsonField
import common.io.json.JsonField.GenType
import common.io.json.Test.JsonTest_0.JsonD
import common.io.json.JsonClass.RType
import java.util.HashSet
import common.io.json.JsonDecoder.OnInjected
import common.io.json.JsonField.IOType
import common.io.json.JsonException
import common.io.json.JsonClass.NoTag
import common.io.json.JsonField.SerType
import common.io.json.JsonClass.WType
import kotlin.reflect.KClass
import com.google.gson.JsonArray
import common.io.assets.Admin.StaticPermitted
import common.io.json.JsonClass.JCGeneric
import common.io.json.JsonClass.JCGetter
import com.google.gson.JsonPrimitive
import com.google.gson.JsonNull
import common.io.json.JsonClass.JCIdentifier
import java.lang.ClassNotFoundException
import common.io.assets.AssetLoader.AssetHeader
import common.io.assets.AssetLoader.AssetHeader.AssetEntry
import common.io.InStreamDef
import common.io.BCUException
import java.io.UnsupportedEncodingException
import common.io.OutStreamDef
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import common.io.PackLoader.FileSaver
import common.system.files.FDByte
import common.io.json.JsonClass.JCConstructor
import common.io.PackLoader.FileLoader.FLStream
import common.io.PackLoader.PatchFile
import java.lang.NullPointerException
import java.lang.IndexOutOfBoundsException
import common.io.MultiStream
import java.io.RandomAccessFile
import common.io.MultiStream.TrueStream
import java.lang.RuntimeException
import common.pack.Source.ResourceLocation
import common.pack.Source.AnimLoader
import common.pack.Source.SourceAnimLoader
import common.util.anim.AnimCI
import common.system.files.FDFile
import common.pack.IndexContainer
import common.battle.data .PCoin
import common.util.pack.EffAnim
import common.battle.data .DataEnemy
import common.util.stage.Limit.DefLimit
import common.pack.IndexContainer.Reductor
import common.pack.FixIndexList.FixIndexMap
import common.pack.VerFixer.IdFixer
import common.pack.IndexContainer.IndexCont
import common.pack.IndexContainer.ContGetter
import common.util.stage.CastleList.PackCasList
import common.util.Data.Proc.THEME
import common.CommonStatic.ImgReader
import common.pack.VerFixer
import common.pack.VerFixer.VerFixerException
import java.lang.NumberFormatException
import common.pack.Source.SourceAnimSaver
import common.pack.VerFixer.EnemyFixer
import common.pack.VerFixer.PackFixer
import common.pack.PackData.DefPack
import java.util.function.BiConsumer
import common.util.BattleStatic
import common.util.anim.AnimU.ImageKeeper
import common.util.anim.AnimCE.AnimCELoader
import common.util.anim.AnimCI.AnimCIKeeper
import common.util.anim.AnimUD.DefImgLoader
import common.util.BattleObj
import common.util.Data.Proc.ProcItem
import common.util.lang.ProcLang.ItemLang
import common.util.lang.LocaleCenter.Displayable
import common.util.lang.Editors.DispItem
import common.util.lang.LocaleCenter.ObjBinder
import common.util.lang.LocaleCenter.ObjBinder.BinderFunc
import common.util.Data.Proc.PROB
import org.jcodec.common.tools.MathUtil
import common.util.Data.Proc.PT
import common.util.Data.Proc.PTD
import common.util.Data.Proc.PM
import common.util.Data.Proc.WAVE
import common.util.Data.Proc.WEAK
import common.util.Data.Proc.STRONG
import common.util.Data.Proc.BURROW
import common.util.Data.Proc.REVIVE
import common.util.Data.Proc.SUMMON
import common.util.Data.Proc.MOVEWAVE
import common.util.Data.Proc.POISON
import common.util.Data.Proc.CRITI
import common.util.Data.Proc.VOLC
import common.util.Data.Proc.ARMOR
import common.util.Data.Proc.SPEED
import java.util.LinkedHashMap
import common.util.lang.LocaleCenter.DisplayItem
import common.util.lang.ProcLang.ProcLangStore
import common.util.lang.Formatter.IntExp
import common.util.lang.Formatter.RefObj
import common.util.lang.Formatter.BoolExp
import common.util.lang.Formatter.BoolElem
import common.util.lang.Formatter.IElem
import common.util.lang.Formatter.Cont
import common.util.lang.Formatter.Elem
import common.util.lang.Formatter.RefElem
import common.util.lang.Formatter.RefField
import common.util.lang.Formatter.RefFunc
import common.util.lang.Formatter.TextRef
import common.util.lang.Formatter.CodeBlock
import common.util.lang.Formatter.TextPlain
import common.util.unit.Unit.UnitInfo
import common.util.lang.MultiLangCont.MultiLangStatics
import common.util.pack.EffAnim.EffType
import common.util.pack.EffAnim.ArmorEff
import common.util.pack.EffAnim.BarEneEff
import common.util.pack.EffAnim.BarrierEff
import common.util.pack.EffAnim.DefEff
import common.util.pack.EffAnim.WarpEff
import common.util.pack.EffAnim.ZombieEff
import common.util.pack.EffAnim.KBEff
import common.util.pack.EffAnim.SniperEff
import common.util.pack.EffAnim.VolcEff
import common.util.pack.EffAnim.SpeedEff
import common.util.pack.EffAnim.WeakUpEff
import common.util.pack.EffAnim.EffAnimStore
import common.util.pack.NyCastle.NyType
import common.util.pack.WaveAnim
import common.util.pack.WaveAnim.WaveType
import common.util.pack.Background.BGWvType
import common.util.unit.Form.FormJson
import common.system.BasedCopable
import common.util.anim.AnimUD
import common.battle.data .DataUnit
import common.battle.entity.EUnit
import common.battle.entity.EEnemy
import common.util.EntRand
import common.util.stage.Recd.Wait
import java.lang.CloneNotSupportedException
import common.util.stage.StageMap.StageMapInfo
import common.util.stage.Stage.StageInfo
import common.util.stage.Limit.PackLimit
import common.util.stage.MapColc.ClipMapColc
import common.util.stage.CastleList.DefCasList
import common.util.stage.MapColc.StItr
import common.util.Data.Proc.IntType.BitCount
import common.util.CopRand
import common.util.LockGL
import java.lang.IllegalAccessException
import common.battle.data .MaskAtk
import common.battle.data .DefaultData
import common.battle.data .DataAtk
import common.battle.data .MaskEntity
import common.battle.data .DataEntity
import common.battle.attack.AtkModelAb
import common.battle.attack.AttackAb
import common.battle.attack.AttackSimple
import common.battle.attack.AttackWave
import common.battle.entity.Cannon
import common.battle.attack.AttackVolcano
import common.battle.attack.ContWaveAb
import common.battle.attack.ContWaveDef
import common.battle.attack.AtkModelEntity
import common.battle.entity.EntCont
import common.battle.attack.ContMove
import common.battle.attack.ContVolcano
import common.battle.attack.ContWaveCanon
import common.battle.attack.AtkModelEnemy
import common.battle.attack.AtkModelUnit
import common.battle.attack.AttackCanon
import common.battle.entity.EUnit.OrbHandler
import common.battle.entity.Entity.AnimManager
import common.battle.entity.Entity.AtkManager
import common.battle.entity.Entity.ZombX
import common.battle.entity.Entity.KBManager
import common.battle.entity.Entity.PoisonToken
import common.battle.entity.Entity.WeakToken
import common.battle.Treasure
import common.battle.MirrorSet
import common.battle.Release
import common.battle.ELineUp
import common.battle.entity.Sniper
import common.battle.entity.ECastle
import java.util.Deque
import common.CommonStatic.Itf
import java.lang.Character
import common.CommonStatic.ImgWriter
import utilpc.awt.FTAT
import utilpc.awt.Blender
import java.awt.RenderingHints
import utilpc.awt.BIBuilder
import java.awt.CompositeContext
import java.awt.image.Raster
import java.awt.image.WritableRaster
import utilpc.ColorSet
import utilpc.OggTimeReader
import utilpc.UtilPC.PCItr.MusicReader
import utilpc.UtilPC.PCItr.PCAL
import javax.swing.UIManager.LookAndFeelInfo
import java.lang.InstantiationException
import javax.swing.UnsupportedLookAndFeelException
import utilpc.Algorithm.ColorShift
import utilpc.Algorithm.StackRect
class EStage(val s: Stage, val star: Int) : BattleObj() {
    val lim: Limit
    val num: IntArray
    val rem: IntArray
    val mul: Double
    private var b: StageBasis? = null

    /** add n new enemies to StageBasis  */
    fun allow(): EEnemy? {
        for (i in rem.indices) {
            val data = s.data.getSimple(i)
            if (inHealth(data) && s.data.allow(b, data.group) && rem[i] == 0 && num[i] != -1) {
                rem[i] = data.respawn_0 + (b.r.nextDouble() * (data.respawn_1 - data.respawn_0)) as Int
                if (num[i] > 0) {
                    num[i]--
                    if (num[i] == 0) num[i] = -1
                }
                if (data.boss == 1) b.shock = true
                val multi = (if (data.multiple == 0) 100 else data.multiple) * mul * 0.01
                val mulatk = (if (data.mult_atk == 0) 100 else data.mult_atk) * mul * 0.01
                val e: AbEnemy = data.enemy.get()
                val ee: EEnemy = e.getEntity(b, data, multi, mulatk, data.layer_0, data.layer_1, data.boss)
                ee.group = data.group
                return ee
            }
        }
        return null
    }

    fun assign(sb: StageBasis?) {
        b = sb
        val datas = s.data.simple
        for (i in rem.indices) {
            rem[i] = datas[i].spawn_0
            if (Math.abs(datas[i].spawn_0) < Math.abs(datas[i].spawn_1)) rem[i] += ((datas[i].spawn_1 - datas[i].spawn_0) * b.r.nextDouble()) as Int
        }
    }

    /** get the Entity representing enemy base, return null if none  */
    fun base(sb: StageBasis?): EEnemy? {
        val ind = num.size - 1
        if (ind < 0) return null
        val data = s.data.getSimple(ind)
        if (data.castle_0 == 0) {
            num[ind] = -1
            val multi = data.multiple * mul * 0.01
            val mulatk = if (data.mult_atk == 0) multi else data.mult_atk * mul * 0.01
            val e: AbEnemy = data.enemy.get()
            return e.getEntity(sb, this, multi, mulatk, data.layer_0, data.layer_1, -1)
        }
        return null
    }

    /** return true if there is still boss in the base  */
    fun hasBoss(): Boolean {
        for (i in rem.indices) {
            val data = s.data.getSimple(i)
            if (data.boss == 1 && num[i] > 0) return true
        }
        return false
    }

    fun update() {
        for (i in rem.indices) {
            val data = s.data.getSimple(i)
            if (inHealth(data) && rem[i] < 0) rem[i] *= -1
            if (rem[i] > 0) rem[i]--
        }
    }

    private fun inHealth(line: SCDef.Line): Boolean {
        val c0 = line.castle_0
        val c1 = line.castle_1
        val d: Double = if (!s.trail) b.getEBHP() * 100 else b.ebase.maxH - b.ebase.health
        return if (c0 >= c1) if (s.trail) d >= c0 else d <= c0 else d > c0 && d <= c1
    }

    init {
        s.validate()
        val datas = s.data.simple
        rem = IntArray(datas.size)
        num = IntArray(datas.size)
        for (i in rem.indices) num[i] = datas[i].number
        lim = s.getLim(star)
        mul = s.map.stars[star] * 0.01
    }
}
