“»
¾
:
Add
x"T
y"T
z"T"
Ttype:
2	
x
Assign
ref"T

value"T

output_ref"T"	
Ttype"
validate_shapebool("
use_lockingbool(
~
BiasAdd

value"T	
bias"T
output"T" 
Ttype:
2	"-
data_formatstringNHWC:
NHWCNCHW
8
Const
output"dtype"
valuetensor"
dtypetype
.
Identity

input"T
output"T"	
Ttype
N
IsVariableInitialized
ref"dtype
is_initialized
"
dtypetype
p
MatMul
a"T
b"T
product"T"
transpose_abool( "
transpose_bbool( "
Ttype:
	2
e
MergeV2Checkpoints
checkpoint_prefixes
destination_prefix"
delete_old_dirsbool(
=
Mul
x"T
y"T
z"T"
Ttype:
2	

NoOp
M
Pack
values"T*N
output"T"
Nint(0"	
Ttype"
axisint 
C
Placeholder
output"dtype"
dtypetype"
shapeshape:
~
RandomUniform

shape"T
output"dtype"
seedint "
seed2int "
dtypetype:
2"
Ttype:
2	
D
Relu
features"T
activations"T"
Ttype:
2	
o
	RestoreV2

prefix
tensor_names
shape_and_slices
tensors2dtypes"
dtypes
list(type)(0
l
SaveV2

prefix
tensor_names
shape_and_slices
tensors2dtypes"
dtypes
list(type)(0
H
ShardedFilename
basename	
shard

num_shards
filename
N

StringJoin
inputs*N

output"
Nint(0"
	separatorstring 
:
Sub
x"T
y"T
z"T"
Ttype:
2	
s

VariableV2
ref"dtype"
shapeshape"
dtypetype"
	containerstring "
shared_namestring "serve*1.9.02b'v1.9.0-0-g25c197e023'
h
inputPlaceholder*'
_output_shapes
:’’’’’’’’’*
dtype0*
shape:’’’’’’’’’
O
outputPlaceholder*
_output_shapes
:*
dtype0*
shape:
q
dense_19_inputPlaceholder*'
_output_shapes
:’’’’’’’’’*
dtype0*
shape:’’’’’’’’’
n
dense_19/random_uniform/shapeConst*
valueB"       *
_output_shapes
:*
dtype0
`
dense_19/random_uniform/minConst*
valueB
 *«ŅČ¾*
_output_shapes
: *
dtype0
`
dense_19/random_uniform/maxConst*
valueB
 *«ŅČ>*
_output_shapes
: *
dtype0
Ŗ
%dense_19/random_uniform/RandomUniformRandomUniformdense_19/random_uniform/shape*
T0*
_output_shapes

: *
dtype0*
seed2«ÕŃ*
seed±’å)
}
dense_19/random_uniform/subSubdense_19/random_uniform/maxdense_19/random_uniform/min*
T0*
_output_shapes
: 

dense_19/random_uniform/mulMul%dense_19/random_uniform/RandomUniformdense_19/random_uniform/sub*
T0*
_output_shapes

: 

dense_19/random_uniformAdddense_19/random_uniform/muldense_19/random_uniform/min*
T0*
_output_shapes

: 

dense_19/kernel
VariableV2*
_output_shapes

: *
dtype0*
shared_name *
shape
: *
	container 
Ą
dense_19/kernel/AssignAssigndense_19/kerneldense_19/random_uniform*
T0*
_output_shapes

: *
use_locking(*
validate_shape(*"
_class
loc:@dense_19/kernel
~
dense_19/kernel/readIdentitydense_19/kernel*
T0*
_output_shapes

: *"
_class
loc:@dense_19/kernel
[
dense_19/ConstConst*
valueB *    *
_output_shapes
: *
dtype0
y
dense_19/bias
VariableV2*
_output_shapes
: *
dtype0*
shared_name *
shape: *
	container 
­
dense_19/bias/AssignAssigndense_19/biasdense_19/Const*
T0*
_output_shapes
: *
use_locking(*
validate_shape(* 
_class
loc:@dense_19/bias
t
dense_19/bias/readIdentitydense_19/bias*
T0*
_output_shapes
: * 
_class
loc:@dense_19/bias

dense_19/MatMulMatMuldense_19_inputdense_19/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:’’’’’’’’’ 

dense_19/BiasAddBiasAdddense_19/MatMuldense_19/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:’’’’’’’’’ 
Y
dense_19/ReluReludense_19/BiasAdd*
T0*'
_output_shapes
:’’’’’’’’’ 
n
dense_20/random_uniform/shapeConst*
valueB"    @   *
_output_shapes
:*
dtype0
`
dense_20/random_uniform/minConst*
valueB
 *  ¾*
_output_shapes
: *
dtype0
`
dense_20/random_uniform/maxConst*
valueB
 *  >*
_output_shapes
: *
dtype0
Ŗ
%dense_20/random_uniform/RandomUniformRandomUniformdense_20/random_uniform/shape*
T0*
_output_shapes

: @*
dtype0*
seed2Ōžŗ*
seed±’å)
}
dense_20/random_uniform/subSubdense_20/random_uniform/maxdense_20/random_uniform/min*
T0*
_output_shapes
: 

dense_20/random_uniform/mulMul%dense_20/random_uniform/RandomUniformdense_20/random_uniform/sub*
T0*
_output_shapes

: @

dense_20/random_uniformAdddense_20/random_uniform/muldense_20/random_uniform/min*
T0*
_output_shapes

: @

dense_20/kernel
VariableV2*
_output_shapes

: @*
dtype0*
shared_name *
shape
: @*
	container 
Ą
dense_20/kernel/AssignAssigndense_20/kerneldense_20/random_uniform*
T0*
_output_shapes

: @*
use_locking(*
validate_shape(*"
_class
loc:@dense_20/kernel
~
dense_20/kernel/readIdentitydense_20/kernel*
T0*
_output_shapes

: @*"
_class
loc:@dense_20/kernel
[
dense_20/ConstConst*
valueB@*    *
_output_shapes
:@*
dtype0
y
dense_20/bias
VariableV2*
_output_shapes
:@*
dtype0*
shared_name *
shape:@*
	container 
­
dense_20/bias/AssignAssigndense_20/biasdense_20/Const*
T0*
_output_shapes
:@*
use_locking(*
validate_shape(* 
_class
loc:@dense_20/bias
t
dense_20/bias/readIdentitydense_20/bias*
T0*
_output_shapes
:@* 
_class
loc:@dense_20/bias

dense_20/MatMulMatMuldense_19/Reludense_20/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:’’’’’’’’’@

dense_20/BiasAddBiasAdddense_20/MatMuldense_20/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:’’’’’’’’’@
Y
dense_20/ReluReludense_20/BiasAdd*
T0*'
_output_shapes
:’’’’’’’’’@
n
dense_21/random_uniform/shapeConst*
valueB"@   @   *
_output_shapes
:*
dtype0
`
dense_21/random_uniform/minConst*
valueB
 *×³]¾*
_output_shapes
: *
dtype0
`
dense_21/random_uniform/maxConst*
valueB
 *×³]>*
_output_shapes
: *
dtype0
Ŗ
%dense_21/random_uniform/RandomUniformRandomUniformdense_21/random_uniform/shape*
T0*
_output_shapes

:@@*
dtype0*
seed2ųļ«*
seed±’å)
}
dense_21/random_uniform/subSubdense_21/random_uniform/maxdense_21/random_uniform/min*
T0*
_output_shapes
: 

dense_21/random_uniform/mulMul%dense_21/random_uniform/RandomUniformdense_21/random_uniform/sub*
T0*
_output_shapes

:@@

dense_21/random_uniformAdddense_21/random_uniform/muldense_21/random_uniform/min*
T0*
_output_shapes

:@@

dense_21/kernel
VariableV2*
_output_shapes

:@@*
dtype0*
shared_name *
shape
:@@*
	container 
Ą
dense_21/kernel/AssignAssigndense_21/kerneldense_21/random_uniform*
T0*
_output_shapes

:@@*
use_locking(*
validate_shape(*"
_class
loc:@dense_21/kernel
~
dense_21/kernel/readIdentitydense_21/kernel*
T0*
_output_shapes

:@@*"
_class
loc:@dense_21/kernel
[
dense_21/ConstConst*
valueB@*    *
_output_shapes
:@*
dtype0
y
dense_21/bias
VariableV2*
_output_shapes
:@*
dtype0*
shared_name *
shape:@*
	container 
­
dense_21/bias/AssignAssigndense_21/biasdense_21/Const*
T0*
_output_shapes
:@*
use_locking(*
validate_shape(* 
_class
loc:@dense_21/bias
t
dense_21/bias/readIdentitydense_21/bias*
T0*
_output_shapes
:@* 
_class
loc:@dense_21/bias

dense_21/MatMulMatMuldense_20/Reludense_21/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:’’’’’’’’’@

dense_21/BiasAddBiasAdddense_21/MatMuldense_21/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:’’’’’’’’’@
Y
dense_21/ReluReludense_21/BiasAdd*
T0*'
_output_shapes
:’’’’’’’’’@
n
dense_22/random_uniform/shapeConst*
valueB"@   @   *
_output_shapes
:*
dtype0
`
dense_22/random_uniform/minConst*
valueB
 *×³]¾*
_output_shapes
: *
dtype0
`
dense_22/random_uniform/maxConst*
valueB
 *×³]>*
_output_shapes
: *
dtype0
Ŗ
%dense_22/random_uniform/RandomUniformRandomUniformdense_22/random_uniform/shape*
T0*
_output_shapes

:@@*
dtype0*
seed2¼«*
seed±’å)
}
dense_22/random_uniform/subSubdense_22/random_uniform/maxdense_22/random_uniform/min*
T0*
_output_shapes
: 

dense_22/random_uniform/mulMul%dense_22/random_uniform/RandomUniformdense_22/random_uniform/sub*
T0*
_output_shapes

:@@

dense_22/random_uniformAdddense_22/random_uniform/muldense_22/random_uniform/min*
T0*
_output_shapes

:@@

dense_22/kernel
VariableV2*
_output_shapes

:@@*
dtype0*
shared_name *
shape
:@@*
	container 
Ą
dense_22/kernel/AssignAssigndense_22/kerneldense_22/random_uniform*
T0*
_output_shapes

:@@*
use_locking(*
validate_shape(*"
_class
loc:@dense_22/kernel
~
dense_22/kernel/readIdentitydense_22/kernel*
T0*
_output_shapes

:@@*"
_class
loc:@dense_22/kernel
[
dense_22/ConstConst*
valueB@*    *
_output_shapes
:@*
dtype0
y
dense_22/bias
VariableV2*
_output_shapes
:@*
dtype0*
shared_name *
shape:@*
	container 
­
dense_22/bias/AssignAssigndense_22/biasdense_22/Const*
T0*
_output_shapes
:@*
use_locking(*
validate_shape(* 
_class
loc:@dense_22/bias
t
dense_22/bias/readIdentitydense_22/bias*
T0*
_output_shapes
:@* 
_class
loc:@dense_22/bias

dense_22/MatMulMatMuldense_21/Reludense_22/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:’’’’’’’’’@

dense_22/BiasAddBiasAdddense_22/MatMuldense_22/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:’’’’’’’’’@
Y
dense_22/ReluReludense_22/BiasAdd*
T0*'
_output_shapes
:’’’’’’’’’@
n
dense_23/random_uniform/shapeConst*
valueB"@      *
_output_shapes
:*
dtype0
`
dense_23/random_uniform/minConst*
valueB
 *¾*
_output_shapes
: *
dtype0
`
dense_23/random_uniform/maxConst*
valueB
 *>*
_output_shapes
: *
dtype0
©
%dense_23/random_uniform/RandomUniformRandomUniformdense_23/random_uniform/shape*
T0*
_output_shapes

:@*
dtype0*
seed2Äą*
seed±’å)
}
dense_23/random_uniform/subSubdense_23/random_uniform/maxdense_23/random_uniform/min*
T0*
_output_shapes
: 

dense_23/random_uniform/mulMul%dense_23/random_uniform/RandomUniformdense_23/random_uniform/sub*
T0*
_output_shapes

:@

dense_23/random_uniformAdddense_23/random_uniform/muldense_23/random_uniform/min*
T0*
_output_shapes

:@

dense_23/kernel
VariableV2*
_output_shapes

:@*
dtype0*
shared_name *
shape
:@*
	container 
Ą
dense_23/kernel/AssignAssigndense_23/kerneldense_23/random_uniform*
T0*
_output_shapes

:@*
use_locking(*
validate_shape(*"
_class
loc:@dense_23/kernel
~
dense_23/kernel/readIdentitydense_23/kernel*
T0*
_output_shapes

:@*"
_class
loc:@dense_23/kernel
[
dense_23/ConstConst*
valueB*    *
_output_shapes
:*
dtype0
y
dense_23/bias
VariableV2*
_output_shapes
:*
dtype0*
shared_name *
shape:*
	container 
­
dense_23/bias/AssignAssigndense_23/biasdense_23/Const*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_23/bias
t
dense_23/bias/readIdentitydense_23/bias*
T0*
_output_shapes
:* 
_class
loc:@dense_23/bias

dense_23/MatMulMatMuldense_22/Reludense_23/kernel/read*
transpose_b( *
T0*
transpose_a( *'
_output_shapes
:’’’’’’’’’

dense_23/BiasAddBiasAdddense_23/MatMuldense_23/bias/read*
T0*
data_formatNHWC*'
_output_shapes
:’’’’’’’’’
\
PlaceholderPlaceholder*
_output_shapes

: *
dtype0*
shape
: 
¤
AssignAssigndense_19/kernelPlaceholder*
T0*
_output_shapes

: *
use_locking( *
validate_shape(*"
_class
loc:@dense_19/kernel
V
Placeholder_1Placeholder*
_output_shapes
: *
dtype0*
shape: 
 
Assign_1Assigndense_19/biasPlaceholder_1*
T0*
_output_shapes
: *
use_locking( *
validate_shape(* 
_class
loc:@dense_19/bias
^
Placeholder_2Placeholder*
_output_shapes

: @*
dtype0*
shape
: @
Ø
Assign_2Assigndense_20/kernelPlaceholder_2*
T0*
_output_shapes

: @*
use_locking( *
validate_shape(*"
_class
loc:@dense_20/kernel
V
Placeholder_3Placeholder*
_output_shapes
:@*
dtype0*
shape:@
 
Assign_3Assigndense_20/biasPlaceholder_3*
T0*
_output_shapes
:@*
use_locking( *
validate_shape(* 
_class
loc:@dense_20/bias
^
Placeholder_4Placeholder*
_output_shapes

:@@*
dtype0*
shape
:@@
Ø
Assign_4Assigndense_21/kernelPlaceholder_4*
T0*
_output_shapes

:@@*
use_locking( *
validate_shape(*"
_class
loc:@dense_21/kernel
V
Placeholder_5Placeholder*
_output_shapes
:@*
dtype0*
shape:@
 
Assign_5Assigndense_21/biasPlaceholder_5*
T0*
_output_shapes
:@*
use_locking( *
validate_shape(* 
_class
loc:@dense_21/bias
^
Placeholder_6Placeholder*
_output_shapes

:@@*
dtype0*
shape
:@@
Ø
Assign_6Assigndense_22/kernelPlaceholder_6*
T0*
_output_shapes

:@@*
use_locking( *
validate_shape(*"
_class
loc:@dense_22/kernel
V
Placeholder_7Placeholder*
_output_shapes
:@*
dtype0*
shape:@
 
Assign_7Assigndense_22/biasPlaceholder_7*
T0*
_output_shapes
:@*
use_locking( *
validate_shape(* 
_class
loc:@dense_22/bias
^
Placeholder_8Placeholder*
_output_shapes

:@*
dtype0*
shape
:@
Ø
Assign_8Assigndense_23/kernelPlaceholder_8*
T0*
_output_shapes

:@*
use_locking( *
validate_shape(*"
_class
loc:@dense_23/kernel
V
Placeholder_9Placeholder*
_output_shapes
:*
dtype0*
shape:
 
Assign_9Assigndense_23/biasPlaceholder_9*
T0*
_output_shapes
:*
use_locking( *
validate_shape(* 
_class
loc:@dense_23/bias

IsVariableInitializedIsVariableInitializeddense_19/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_19/kernel

IsVariableInitialized_1IsVariableInitializeddense_19/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_19/bias

IsVariableInitialized_2IsVariableInitializeddense_20/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_20/kernel

IsVariableInitialized_3IsVariableInitializeddense_20/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_20/bias

IsVariableInitialized_4IsVariableInitializeddense_21/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_21/kernel

IsVariableInitialized_5IsVariableInitializeddense_21/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_21/bias

IsVariableInitialized_6IsVariableInitializeddense_22/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_22/kernel

IsVariableInitialized_7IsVariableInitializeddense_22/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_22/bias

IsVariableInitialized_8IsVariableInitializeddense_23/kernel*
_output_shapes
: *
dtype0*"
_class
loc:@dense_23/kernel

IsVariableInitialized_9IsVariableInitializeddense_23/bias*
_output_shapes
: *
dtype0* 
_class
loc:@dense_23/bias
ü
initNoOp^dense_19/bias/Assign^dense_19/kernel/Assign^dense_20/bias/Assign^dense_20/kernel/Assign^dense_21/bias/Assign^dense_21/kernel/Assign^dense_22/bias/Assign^dense_22/kernel/Assign^dense_23/bias/Assign^dense_23/kernel/Assign
P

save/ConstConst*
valueB Bmodel*
_output_shapes
: *
dtype0

save/StringJoin/inputs_1Const*<
value3B1 B+_temp_eeb4f93aa8ee4f369a6d2673b649e305/part*
_output_shapes
: *
dtype0
u
save/StringJoin
StringJoin
save/Constsave/StringJoin/inputs_1*
	separator *
N*
_output_shapes
: 
Q
save/num_shardsConst*
value	B :*
_output_shapes
: *
dtype0
k
save/ShardedFilename/shardConst"/device:CPU:0*
value	B : *
_output_shapes
: *
dtype0

save/ShardedFilenameShardedFilenamesave/StringJoinsave/ShardedFilename/shardsave/num_shards"/device:CPU:0*
_output_shapes
: 

save/SaveV2/tensor_namesConst"/device:CPU:0*µ
value«BØ
Bdense_19/biasBdense_19/kernelBdense_20/biasBdense_20/kernelBdense_21/biasBdense_21/kernelBdense_22/biasBdense_22/kernelBdense_23/biasBdense_23/kernel*
_output_shapes
:
*
dtype0

save/SaveV2/shape_and_slicesConst"/device:CPU:0*'
valueB
B B B B B B B B B B *
_output_shapes
:
*
dtype0
¬
save/SaveV2SaveV2save/ShardedFilenamesave/SaveV2/tensor_namessave/SaveV2/shape_and_slicesdense_19/biasdense_19/kerneldense_20/biasdense_20/kerneldense_21/biasdense_21/kerneldense_22/biasdense_22/kerneldense_23/biasdense_23/kernel"/device:CPU:0*
dtypes
2

 
save/control_dependencyIdentitysave/ShardedFilename^save/SaveV2"/device:CPU:0*
T0*
_output_shapes
: *'
_class
loc:@save/ShardedFilename
¬
+save/MergeV2Checkpoints/checkpoint_prefixesPacksave/ShardedFilename^save/control_dependency"/device:CPU:0*

axis *
T0*
N*
_output_shapes
:

save/MergeV2CheckpointsMergeV2Checkpoints+save/MergeV2Checkpoints/checkpoint_prefixes
save/Const"/device:CPU:0*
delete_old_dirs(

save/IdentityIdentity
save/Const^save/MergeV2Checkpoints^save/control_dependency"/device:CPU:0*
T0*
_output_shapes
: 

save/RestoreV2/tensor_namesConst"/device:CPU:0*µ
value«BØ
Bdense_19/biasBdense_19/kernelBdense_20/biasBdense_20/kernelBdense_21/biasBdense_21/kernelBdense_22/biasBdense_22/kernelBdense_23/biasBdense_23/kernel*
_output_shapes
:
*
dtype0

save/RestoreV2/shape_and_slicesConst"/device:CPU:0*'
valueB
B B B B B B B B B B *
_output_shapes
:
*
dtype0
Ģ
save/RestoreV2	RestoreV2
save/Constsave/RestoreV2/tensor_namessave/RestoreV2/shape_and_slices"/device:CPU:0*
dtypes
2
*<
_output_shapes*
(::::::::::
¤
save/AssignAssigndense_19/biassave/RestoreV2*
T0*
_output_shapes
: *
use_locking(*
validate_shape(* 
_class
loc:@dense_19/bias
°
save/Assign_1Assigndense_19/kernelsave/RestoreV2:1*
T0*
_output_shapes

: *
use_locking(*
validate_shape(*"
_class
loc:@dense_19/kernel
Ø
save/Assign_2Assigndense_20/biassave/RestoreV2:2*
T0*
_output_shapes
:@*
use_locking(*
validate_shape(* 
_class
loc:@dense_20/bias
°
save/Assign_3Assigndense_20/kernelsave/RestoreV2:3*
T0*
_output_shapes

: @*
use_locking(*
validate_shape(*"
_class
loc:@dense_20/kernel
Ø
save/Assign_4Assigndense_21/biassave/RestoreV2:4*
T0*
_output_shapes
:@*
use_locking(*
validate_shape(* 
_class
loc:@dense_21/bias
°
save/Assign_5Assigndense_21/kernelsave/RestoreV2:5*
T0*
_output_shapes

:@@*
use_locking(*
validate_shape(*"
_class
loc:@dense_21/kernel
Ø
save/Assign_6Assigndense_22/biassave/RestoreV2:6*
T0*
_output_shapes
:@*
use_locking(*
validate_shape(* 
_class
loc:@dense_22/bias
°
save/Assign_7Assigndense_22/kernelsave/RestoreV2:7*
T0*
_output_shapes

:@@*
use_locking(*
validate_shape(*"
_class
loc:@dense_22/kernel
Ø
save/Assign_8Assigndense_23/biassave/RestoreV2:8*
T0*
_output_shapes
:*
use_locking(*
validate_shape(* 
_class
loc:@dense_23/bias
°
save/Assign_9Assigndense_23/kernelsave/RestoreV2:9*
T0*
_output_shapes

:@*
use_locking(*
validate_shape(*"
_class
loc:@dense_23/kernel
ø
save/restore_shardNoOp^save/Assign^save/Assign_1^save/Assign_2^save/Assign_3^save/Assign_4^save/Assign_5^save/Assign_6^save/Assign_7^save/Assign_8^save/Assign_9
-
save/restore_allNoOp^save/restore_shard"<
save/Const:0save/Identity:0save/restore_all (5 @F8"¤
trainable_variables
`
dense_19/kernel:0dense_19/kernel/Assigndense_19/kernel/read:02dense_19/random_uniform:08
Q
dense_19/bias:0dense_19/bias/Assigndense_19/bias/read:02dense_19/Const:08
`
dense_20/kernel:0dense_20/kernel/Assigndense_20/kernel/read:02dense_20/random_uniform:08
Q
dense_20/bias:0dense_20/bias/Assigndense_20/bias/read:02dense_20/Const:08
`
dense_21/kernel:0dense_21/kernel/Assigndense_21/kernel/read:02dense_21/random_uniform:08
Q
dense_21/bias:0dense_21/bias/Assigndense_21/bias/read:02dense_21/Const:08
`
dense_22/kernel:0dense_22/kernel/Assigndense_22/kernel/read:02dense_22/random_uniform:08
Q
dense_22/bias:0dense_22/bias/Assigndense_22/bias/read:02dense_22/Const:08
`
dense_23/kernel:0dense_23/kernel/Assigndense_23/kernel/read:02dense_23/random_uniform:08
Q
dense_23/bias:0dense_23/bias/Assigndense_23/bias/read:02dense_23/Const:08"
	variables
`
dense_19/kernel:0dense_19/kernel/Assigndense_19/kernel/read:02dense_19/random_uniform:08
Q
dense_19/bias:0dense_19/bias/Assigndense_19/bias/read:02dense_19/Const:08
`
dense_20/kernel:0dense_20/kernel/Assigndense_20/kernel/read:02dense_20/random_uniform:08
Q
dense_20/bias:0dense_20/bias/Assigndense_20/bias/read:02dense_20/Const:08
`
dense_21/kernel:0dense_21/kernel/Assigndense_21/kernel/read:02dense_21/random_uniform:08
Q
dense_21/bias:0dense_21/bias/Assigndense_21/bias/read:02dense_21/Const:08
`
dense_22/kernel:0dense_22/kernel/Assigndense_22/kernel/read:02dense_22/random_uniform:08
Q
dense_22/bias:0dense_22/bias/Assigndense_22/bias/read:02dense_22/Const:08
`
dense_23/kernel:0dense_23/kernel/Assigndense_23/kernel/read:02dense_23/random_uniform:08
Q
dense_23/bias:0dense_23/bias/Assigndense_23/bias/read:02dense_23/Const:08*Z
serving_defaultG
'
input
input:0’’’’’’’’’
output
output:0